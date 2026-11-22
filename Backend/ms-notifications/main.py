import os
import socket
import logging
from contextlib import asynccontextmanager
from fastapi import FastAPI, Response, status
import py_eureka_client.eureka_client as eureka_client

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("ms-notifications")

# Configurations
PORT = int(os.getenv("PORT", 8085))
EUREKA_SERVER = os.getenv("EUREKA_SERVER", "http://localhost:8761/eureka")
APP_NAME = os.getenv("APP_NAME", "MS-NOTIFICATIONS")

# Détermination de l'adresse IP locale accessible
def get_local_ip():
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        ip = s.getsockname()[0]
        s.close()
        return ip
    except Exception:
        return "127.0.0.1"

HOST_IP = os.getenv("HOST_IP", get_local_ip())

# État de RabbitMQ (simulable pour tests de résilience)
rabbitmq_connected = True

def check_rabbitmq_connection() -> bool:
    global rabbitmq_connected
    return rabbitmq_connected

@asynccontextmanager
async def lifespan(app: FastAPI):
    # Enregistrement auprès d'Eureka avec URLs de santé et d'informations
    health_url = f"http://{HOST_IP}:{PORT}/health"
    status_url = f"http://{HOST_IP}:{PORT}/info"
    home_url = f"http://{HOST_IP}:{PORT}/"

    logger.info(f"Enregistrement auprès d'Eureka Server ({EUREKA_SERVER}) sous le nom {APP_NAME}...")
    logger.info(f"Health Check URL: {health_url} | Status Page URL: {status_url}")

    try:
        await eureka_client.init_async(
            eureka_server=EUREKA_SERVER,
            app_name=APP_NAME,
            instance_port=PORT,
            instance_host=HOST_IP,
            health_check_url=health_url,
            status_page_url=status_url,
            home_page_url=home_url
        )
        logger.info(f"Service {APP_NAME} enregistré avec succès sur Eureka.")
    except Exception as e:
        logger.error(f"Erreur lors de l'enregistrement Eureka : {e}")

    yield

    # Arrêt propre
    logger.info("Arrêt de l'application et désenregistrement propre d'Eureka...")
    try:
        await eureka_client.stop_async()
        logger.info("Désenregistrement Eureka effectué.")
    except Exception as e:
        logger.error(f"Erreur lors du désenregistrement Eureka : {e}")

app = FastAPI(title="MS-NOTIFICATIONS", lifespan=lifespan)

@app.get("/")
def root():
    return {"message": "Bienvenue sur le service MS-NOTIFICATIONS"}

@app.get("/health")
def health(response: Response):
    rabbit_ok = check_rabbitmq_connection()
    if rabbit_ok:
        return {
            "status": "UP",
            "components": {
                "rabbitmq": {"status": "UP", "details": "Connexion RabbitMQ établie"},
                "diskSpace": {"status": "UP"}
            }
        }
    else:
        response.status_code = status.HTTP_503_SERVICE_UNAVAILABLE
        return {
            "status": "DOWN",
            "components": {
                "rabbitmq": {"status": "DOWN", "details": "Connexion RabbitMQ perdue"}
            }
        }

@app.get("/info")
def info():
    return {
        "app": {
            "name": "ms-notifications",
            "version": "1.0.0",
            "description": "Microservice de notifications individuelles, rappels et temps réel (SSE/RabbitMQ)",
            "team": "Groupe 2 - 4DAS11"
        }
    }

@app.get("/api/notifications/hello")
def hello():
    return {
        "service": APP_NAME,
        "status": "UP",
        "message": "Service des notifications opérationnel"
    }

@app.post("/api/notifications/simulate-rabbitmq")
def simulate_rabbitmq(ok: bool = False):
    global rabbitmq_connected
    rabbitmq_connected = ok
    logger.warning(f"SIMULATION: État de RabbitMQ modifié à {ok}")
    return {
        "rabbitmqConnected": rabbitmq_connected,
        "serviceHealth": "UP" if rabbitmq_connected else "DOWN"
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=PORT, reload=False)
