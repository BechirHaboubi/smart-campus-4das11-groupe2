@echo off
title Microservice Notification - Python / FastAPI (Port 8084)
set "PATH=C:\Users\hp\anaconda3;C:\Users\hp\anaconda3\Scripts;%PATH%"

echo ====================================================
echo  Demarrage du Microservice Notification (Port 8084)...
echo ====================================================
cd /d "%~dp0notification"
python main.py
pause
