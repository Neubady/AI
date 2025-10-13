@ECHO OFF
SET DIR=%~dp0
SET APP_BASE_NAME=%~n0
SET APP_HOME=%DIR%
"%DIR%\gradle\wrapper\gradle-wrapper.jar" %*
