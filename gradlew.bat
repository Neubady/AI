@echo off
setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0
set WRAPPER_DIR=%SCRIPT_DIR%gradle\wrapper
set PROPS_FILE=%WRAPPER_DIR%gradle-wrapper.properties

if not exist "%PROPS_FILE%" (
    echo gradle-wrapper.properties not found
    exit /b 1
)

for /f "tokens=1,* delims==" %%A in ('findstr /b "distributionUrl" "%PROPS_FILE%"') do set DIST_URL=%%B
set DIST_URL=%DIST_URL:\:=:%
for /f "tokens=1,* delims==" %%A in ('findstr /b "distributionSha256Sum" "%PROPS_FILE%"') do set DIST_SHA=%%B

if "%DIST_URL%"=="" (
    echo distributionUrl is not defined in gradle-wrapper.properties
    exit /b 1
)

for %%A in ("%DIST_URL%") do set DIST_NAME=%%~nxA
set DIST_BASE=%DIST_NAME:.zip=%
for /f "tokens=2 delims=-" %%A in ("%DIST_NAME%") do set VERSION=%%A

if "%GRADLE_USER_HOME%"=="" set GRADLE_USER_HOME=%USERPROFILE%\.gradle
set DOWNLOAD_DIR=%GRADLE_USER_HOME%\wrapper\dists
set DIST_DIR=%DOWNLOAD_DIR%\%DIST_BASE%
set INSTALL_DIR=%DIST_DIR%\gradle-%VERSION%
set ZIP_PATH=%DOWNLOAD_DIR%\%DIST_NAME%

if not exist "%DOWNLOAD_DIR%" mkdir "%DOWNLOAD_DIR%"

if not exist "%INSTALL_DIR%" (
    if not exist "%ZIP_PATH%" (
        echo Downloading Gradle distribution %DIST_NAME%...
        powershell -Command "(New-Object System.Net.WebClient).DownloadFile('%DIST_URL%', '%ZIP_PATH%')"
        if errorlevel 1 exit /b 1
    )

    if not "%DIST_SHA%"=="" (
        for /f "delims=" %%S in ('powershell -Command "Get-FileHash -Algorithm SHA256 '%ZIP_PATH%' ^| Select-Object -ExpandProperty Hash"') do set ACTUAL_SHA=%%S
        if /I not "%ACTUAL_SHA%"=="%DIST_SHA%" (
            echo Checksum verification failed
            del "%ZIP_PATH%"
            exit /b 1
        )
    )

    echo Extracting Gradle...
    if exist "%DIST_DIR%" rmdir /s /q "%DIST_DIR%"
    mkdir "%DIST_DIR%"
    powershell -Command "Add-Type -AssemblyName System.IO.Compression.FileSystem; [System.IO.Compression.ZipFile]::ExtractToDirectory('%ZIP_PATH%', '%DIST_DIR%')"
)

set GRADLE_BIN=%INSTALL_DIR%\bin\gradle.bat
if not exist "%GRADLE_BIN%" (
    echo Gradle executable not found at %GRADLE_BIN%
    exit /b 1
)

call "%GRADLE_BIN%" %*
