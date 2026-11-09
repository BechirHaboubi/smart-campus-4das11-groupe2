@echo off
title Microservice Candidat (Port 8081)
set "JAVA_HOME=C:\Users\hp\.jdks\ms-17.0.18"
set "PATH=%JAVA_HOME%\bin;C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.2\plugins\maven\lib\maven3\bin;%PATH%"

echo ====================================================
echo  Demarrage du Microservice Candidat (Port 8081)...
echo ====================================================
cd /d "%~dp0candidat"
call mvn spring-boot:run
pause
