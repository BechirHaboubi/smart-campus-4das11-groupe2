@echo off
title Microservice Job (Port 8082)
set "JAVA_HOME=C:\Users\hp\.jdks\ms-17.0.18"
set "PATH=%JAVA_HOME%\bin;C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.2\plugins\maven\lib\maven3\bin;%PATH%"

echo ====================================================
echo  Demarrage du Microservice Job (Port 8082)...
echo  (Assurez-vous que MySQL est demarre dans XAMPP/WAMP)
echo ====================================================
cd /d "%~dp0job"
call mvn spring-boot:run
pause
