import os
import sys
import logging
from contextlib import asynccontextmanager
from fastapi import FastAPI
from fastapi.responses import JSONResponse
import py_eureka_client.eureka_client as eureka_client

logging.basicConfig(level=logging.INFO, format="%(asctime)s [%(levelname)s] %(message)s")
logger = logging.getLogger("notification-service")

PORT = int(os.getenv("PORT", 8084))
HOST = os.getenv("HOST", "localhost")
EUREKA_SERVER = os.getenv("EUREKA_SERVER", "http://localhost:8761/eureka")
APP_NAME = "NOTIFICATION"

@asynccontextmanager
async def lifespan(app: FastAPI):
    # Démarrage : Enregistrement auprès d'Eureka
    logger.info(f"Démarrage du microservice {APP_NAME} sur le port {PORT}...")
    try:
        await eureka_client.init_async(
            eureka_server=EUREKA_SERVER,
            app_name=APP_NAME,
            instance_port=PORT,
            instance_host=HOST,
            instance_ip="127.0.0.1",
            health_check_url=f"http://{HOST}:{PORT}/health",
            status_page_url=f"http://{HOST}:{PORT}/info",
            home_page_url=f"http://{HOST}:{PORT}/api/notifications/hello"
        )
        logger.info(f"Microservice {APP_NAME} enregistré avec succès dans Eureka !")
    except Exception as e:
        logger.warning(f"Impossible d'enregistrer {APP_NAME} auprès d'Eureka ({e}). Le serveur continuera de tourner.")

    yield

    # Arrêt : Dé-enregistrement propre d'Eureka
    logger.info(f"Arrêt du microservice {APP_NAME} et désinscription d'Eureka...")
    try:
        await eureka_client.stop_async()
        logger.info(f"Microservice {APP_NAME} désinscrit d'Eureka.")
    except Exception as e:
        logger.error(f"Erreur lors de la désinscription d'Eureka: {e}")

app = FastAPI(
    title="Notification Microservice",
    version="1.0.0",
    description="Microservice de notification (Python / FastAPI) enregistré dans Eureka avec Actuator /health /info",
    lifespan=lifespan
)

@app.get("/health")
async def health():
    return JSONResponse(status_code=200, content={"status": "UP"})

@app.get("/info")
async def info():
    return {
        "app": {
            "name": "notification",
            "version": "1.0.0",
            "description": "Microservice de gestion des notifications en temps reel",
            "team": "Groupe 2 - 4DAS11"
        }
    }

@app.get("/api/notifications/hello")
@app.get("/api/notifications")
async def hello():
    return {
        "message": "Hello from Notification service (Python / FastAPI)!",
        "port": PORT,
        "service": APP_NAME,
        "status": "UP"
    }

@app.get("/")
async def root():
    return {
        "service": APP_NAME,
        "port": PORT,
        "endpoints": [
            "/health",
            "/info",
            "/api/notifications/hello"
        ]
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=PORT, reload=False)
