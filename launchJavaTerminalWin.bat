@echo off
REM It seems that Console settings are stored by window name
REM Extra reference: https://stackoverflow.com/questions/34252265/how-to-start-mingw-console-gitbash-from-command-line-on-windows

start "Java terminal" cmd /K "setJavaEnv.bat"