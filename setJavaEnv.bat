@echo off
REM This script is used by launchJavaTerminalWin.bat and can be run manually

echo Setting up Java environment...
echo.
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_181
echo Set JAVA_HOME to %JAVA_HOME%
set PATH=%JAVA_HOME%\bin;%PATH%
echo Prepended JAVA_HOME\bin to PATH
