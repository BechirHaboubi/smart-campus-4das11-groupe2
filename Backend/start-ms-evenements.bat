@echo off
title Microservice Evenements - Smart Campus (Port 8082)
set "JAVA_HOME=C:\Users\hp\.jdks\ms-17.0.18"
set "PATH=%JAVA_HOME%\bin;C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.2\plugins\maven\lib\maven3\bin;%PATH%"

echo ====================================================
echo  Demarrage du Microservice Evenements (Port 8082)...
echo  (Smart Campus - Dhia Trabelsi)
echo ====================================================
cd /d "%~dp0ms-evenements"
call mvn spring-boot:run
pause
