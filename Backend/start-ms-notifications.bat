@echo off
title MS-NOTIFICATIONS (Port 8085) - Equipe Smart Campus
set "JAVA_HOME=C:\Users\hp\.jdks\ms-17.0.18"
set "PATH=%JAVA_HOME%\bin;C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.2\plugins\maven\lib\maven3\bin;%PATH%"

echo ====================================================
echo  Demarrage de ms-notifications (Port 8085)...
echo ====================================================
cd /d "%~dp0ms-notifications"
call mvn spring-boot:run
pause
