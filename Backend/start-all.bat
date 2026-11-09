@echo off
title Lancement Global des Microservices AWD
echo ===================================================================
echo     Demarrage de l'ecosysteme Eureka + Actuator (AWD 2026-2027)
echo ===================================================================
echo.
echo Rappel : Si vous testez la persistance MySQL pour Job / Evenements,
echo assurez-vous d'avoir demarre MySQL dans XAMPP ou WAMP.
echo.
echo 1. Lancement du serveur Eureka (Port 8761)...
start "Eureka Server [8761]" "%~dp0start-eureka.bat"
timeout /t 10 /nobreak >nul

echo 2. Lancement de Candidat (Port 8081)...
start "Candidat [8081]" "%~dp0start-candidat.bat"

echo 3. Lancement de Job (Port 8082)...
start "Job [8082]" "%~dp0start-job.bat"

echo 4. Lancement de Candidature (Port 8085)...
start "Candidature [8085]" "%~dp0start-candidature.bat"

echo 5. Lancement de Meeting Node.js (Port 8083)...
start "Meeting [8083]" "%~dp0start-meeting.bat"

echo 6. Lancement de Notification Python (Port 8084)...
start "Notification [8084]" "%~dp0start-notification.bat"

echo.
echo ===================================================================
echo Tous les services sont en cours de demarrage !
echo Ouvrez le tableau de bord Eureka dans votre navigateur :
echo http://localhost:8761
echo ===================================================================
pause
