@echo off
title Microservice Meeting - Node.js (Port 8083)
set "PATH=C:\Users\hp\AppData\Local\ms-playwright-go\1.57.0;%PATH%"

echo ====================================================
echo  Demarrage du Microservice Meeting sur le port 8083...
echo ====================================================
cd /d "%~dp0meeting"
node index.js
pause
