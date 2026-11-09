@echo off
title Lancement Global des Microservices Smart Campus
echo ===================================================================
echo     Demarrage de l'ecosysteme Smart Campus (AWD 2026-2027)
echo ===================================================================
echo.
echo 1. Lancement du serveur Eureka (Port 8761)...
start "Eureka Server [8761]" "%~dp0start-eureka.bat"
timeout /t 8 /nobreak >nul

echo 2. Lancement de ms-reservations (Port 8081 - Bechir)...
start "ms-reservations [8081]" "%~dp0start-ms-reservations.bat"

echo 3. Lancement de ms-evenements (Port 8082 - Dhia)...
start "ms-evenements [8082]" "%~dp0start-ms-evenements.bat"

echo 4. Lancement de ms-annonces (Port 8083 - Yassine)...
start "ms-annonces [8083]" "%~dp0start-ms-annonces.bat"

echo 5. Lancement de ms-objets-perdus (Port 8084 - Chadha)...
start "ms-objets-perdus [8084]" "%~dp0start-ms-objets-perdus.bat"

echo 6. Lancement de ms-notifications (Port 8085 - Equipe)...
start "ms-notifications [8085]" "%~dp0start-ms-notifications.bat"

echo.
echo ===================================================================
echo Tous les microservices officiels Smart Campus sont en cours de lancement !
echo Ouvrez Eureka Dashboard : http://localhost:8761
echo ===================================================================
pause
