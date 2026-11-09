@echo off
title MS-OBJETS-PERDUS (Port 8084) - Chadha Dalhoumi
set "JAVA_HOME=C:\Users\hp\.jdks\ms-17.0.18"
set "PATH=%JAVA_HOME%\bin;C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.2\plugins\maven\lib\maven3\bin;%PATH%"

echo ====================================================
echo  Demarrage de ms-objets-perdus (Port 8084)...
echo ====================================================
cd /d "%~dp0ms-objets-perdus"
call mvn spring-boot:run
pause
