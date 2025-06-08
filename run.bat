@echo off
title Online Medical Appointment
cd /d "%~dp0"
echo Starting Online Medical Appointment...
java -jar "target\OnlineMedicalAppointment-1.0-SNAPSHOT.jar"
if %errorlevel% neq 0 (
    echo.
    echo Error: Failed to start the application.
    echo Please ensure you have Java 17 or later installed.
    echo You can download it from: https://adoptium.net/
)
pause
