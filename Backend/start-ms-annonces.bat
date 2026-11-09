@echo off
title MS-ANNONCES (Port 8083) - Yassine Gharbi
set "JAVA_HOME=C:\Users\hp\.jdks\ms-17.0.18"
set "PATH=%JAVA_HOME%\bin;C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.2\plugins\maven\lib\maven3\bin;%PATH%"

echo ====================================================
echo  Demarrage de ms-annonces (Port 8083)...
echo ====================================================
cd /d "%~dp0ms-annonces"
call mvn spring-boot:run
pause
