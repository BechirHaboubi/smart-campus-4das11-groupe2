@echo off
title Eureka Server (Port 8761)
set "JAVA_HOME=C:\Users\hp\.jdks\ms-17.0.18"
set "PATH=%JAVA_HOME%\bin;C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.2\plugins\maven\lib\maven3\bin;%PATH%"

echo ====================================================
echo  Demarrage d'Eureka Server sur le port 8761...
echo ====================================================
cd /d "%~dp0eureka-server"
call mvn spring-boot:run
pause
