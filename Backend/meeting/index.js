const http = require('http');
const path = require('path');
const pkg = require('./package.json');

const PORT = parseInt(process.env.PORT, 10) || 8083;
const HOST = process.env.HOST || 'localhost';
const EUREKA_HOST = process.env.EUREKA_HOST || 'localhost';
const EUREKA_PORT = parseInt(process.env.EUREKA_PORT, 10) || 8761;
const APP_NAME = 'MEETING';
const INSTANCE_ID = `${HOST}:${APP_NAME.toLowerCase()}:${PORT}`;

// Simple Express or native HTTP request dispatcher
function handleRequest(req, res) {
  const url = req.url.split('?')[0];

  // Enable JSON response helper
  res.sendJson = (statusCode, data) => {
    res.writeHead(statusCode, {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*'
    });
    res.end(JSON.stringify(data, null, 2));
  };

  // Routes
  if (req.method === 'GET' && url === '/health') {
    return res.sendJson(200, {
      status: 'UP',
      components: {
        diskSpace: { status: 'UP' },
        ping: { status: 'UP' }
      }
    });
  }

  if (req.method === 'GET' && url === '/info') {
    return res.sendJson(200, {
      app: {
        name: pkg.name,
        version: pkg.version,
        description: pkg.description,
        team: 'Groupe 2 - 4DAS11'
      }
    });
  }

  if (req.method === 'GET' && (url === '/api/meetings/hello' || url === '/api/meetings')) {
    return res.sendJson(200, {
      message: 'Hello from Meeting service (Node.js / Express)!',
      port: PORT,
      instanceId: INSTANCE_ID,
      timestamp: new Date().toISOString()
    });
  }

  // Root fallback
  if (req.method === 'GET' && url === '/') {
    return res.sendJson(200, {
      service: APP_NAME,
      port: PORT,
      status: 'UP',
      endpoints: [
        '/health',
        '/info',
        '/api/meetings/hello'
      ]
    });
  }

  res.sendJson(404, { error: 'Not Found', path: url });
}

// Start HTTP server
const server = http.createServer(handleRequest);
server.listen(PORT, () => {
  console.log(`[Meeting Service] Server running on http://${HOST}:${PORT}`);
  startEurekaRegistration();
});

// Eureka Client Integration
let eurekaHeartbeatTimer = null;

function startEurekaRegistration() {
  // Check if eureka-js-client is installed
  try {
    const Eureka = require('eureka-js-client').Eureka;
    console.log('[Meeting Service] Utilisation de eureka-js-client...');
    const client = new Eureka({
      instance: {
        app: APP_NAME,
        hostName: HOST,
        ipAddr: '127.0.0.1',
        statusPageUrl: `http://${HOST}:${PORT}/info`,
        healthCheckUrl: `http://${HOST}:${PORT}/health`,
        homePageUrl: `http://${HOST}:${PORT}/api/meetings/hello`,
        port: {
          '$': PORT,
          '@enabled': 'true'
        },
        vipAddress: APP_NAME.toLowerCase(),
        dataCenterInfo: {
          '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
          name: 'MyOwn'
        }
      },
      eureka: {
        host: EUREKA_HOST,
        port: EUREKA_PORT,
        servicePath: '/eureka/apps/',
        maxRetries: 10,
        requestRetryDelay: 2000
      }
    });

    client.start((err) => {
      if (err) {
        console.error('[Meeting Service] Erreur d enregistrement Eureka (eureka-js-client):', err.message);
        fallbackDirectEureka();
      } else {
        console.log(`[Meeting Service] Enregistre avec succes dans Eureka sous le nom ${APP_NAME} (eureka-js-client) !`);
      }
    });

    process.on('SIGINT', () => {
      console.log('\n[Meeting Service] Arret et deregistration de Eureka...');
      client.stop(() => process.exit(0));
    });
    return;
  } catch (e) {
    console.log('[Meeting Service] eureka-js-client non trouve, utilisation de l enregistrement Eureka REST natif.');
    fallbackDirectEureka();
  }
}

// Fallback direct REST client towards Netflix Eureka (standard protocol)
function fallbackDirectEureka() {
  const instanceData = {
    instance: {
      instanceId: INSTANCE_ID,
      hostName: HOST,
      app: APP_NAME,
      ipAddr: '127.0.0.1',
      status: 'UP',
      overriddenStatus: 'UNKNOWN',
      port: { '$': PORT, '@enabled': 'true' },
      securePort: { '$': 443, '@enabled': 'false' },
      countryId: 1,
      dataCenterInfo: {
        '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
        name: 'MyOwn'
      },
      homePageUrl: `http://${HOST}:${PORT}/api/meetings/hello`,
      statusPageUrl: `http://${HOST}:${PORT}/info`,
      healthCheckUrl: `http://${HOST}:${PORT}/health`,
      vipAddress: APP_NAME.toLowerCase(),
      secureVipAddress: APP_NAME.toLowerCase(),
      leaseInfo: {
        renewalIntervalInSecs: 30,
        durationInSecs: 90
      }
    }
  };

  function register() {
    const payload = JSON.stringify(instanceData);
    const req = http.request({
      hostname: EUREKA_HOST,
      port: EUREKA_PORT,
      path: `/eureka/apps/${APP_NAME}`,
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Content-Length': Buffer.byteLength(payload)
      }
    }, (res) => {
      if (res.statusCode === 204 || res.statusCode === 200) {
        console.log(`[Meeting Service] Enregistre dans Eureka avec succes (${APP_NAME})`);
      } else {
        console.log(`[Meeting Service] Reponse Eureka: ${res.statusCode}`);
      }
    });

    req.on('error', (err) => {
      console.log(`[Meeting Service] En attente du serveur Eureka (${EUREKA_HOST}:${EUREKA_PORT})...`);
    });

    req.write(payload);
    req.end();
  }

  function sendHeartbeat() {
    const req = http.request({
      hostname: EUREKA_HOST,
      port: EUREKA_PORT,
      path: `/eureka/apps/${APP_NAME}/${INSTANCE_ID}`,
      method: 'PUT'
    }, (res) => {
      if (res.statusCode === 404) {
        // Re-register if lease expired or server restarted
        register();
      }
    });

    req.on('error', () => {});
    req.end();
  }

  function deregister(callback) {
    const req = http.request({
      hostname: EUREKA_HOST,
      port: EUREKA_PORT,
      path: `/eureka/apps/${APP_NAME}/${INSTANCE_ID}`,
      method: 'DELETE'
    }, () => {
      if (callback) callback();
    });
    req.on('error', () => {
      if (callback) callback();
    });
    req.end();
  }

  register();
  eurekaHeartbeatTimer = setInterval(sendHeartbeat, 30000);

  process.on('SIGINT', () => {
    console.log('\n[Meeting Service] Arret et suppression du registre Eureka...');
    if (eurekaHeartbeatTimer) clearInterval(eurekaHeartbeatTimer);
    deregister(() => process.exit(0));
  });
}
