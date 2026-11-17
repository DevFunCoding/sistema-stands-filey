@echo off
if exist out rmdir /s /q out
mkdir out

dir /s /B src\*.java > sources.txt
javac -cp ".;lib\mysql-connector-j-26.10.0.jar" -d out @sources.txt
del sources.txt

if %errorlevel% equ 0 (
    start javaw -cp "out;lib\mysql-connector-j-26.10.0.jar" com.filey.main.Main
) else (
    echo Error de compilacion. Revisa los mensajes anteriores.
    pause
)