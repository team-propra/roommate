@echo off
setlocal DisableDelayedExpansion

if not exist ".env" (
    echo ERROR: .env not found.
    exit /b 1
)

for /f "usebackq tokens=* delims=" %%L in (".env") do (
    set "line=%%L"
    call :processLine
    if errorlevel 1 exit /b 1
)

echo Loaded .env

cmd /k

exit /b 0

:processLine
for /f "tokens=* delims= " %%A in ("%line%") do set "trimmed=%%A"

rem Skip blank lines and comments after leading whitespace.
if "%trimmed%"=="" exit /b 0
if "%trimmed:~0,1%"=="#" exit /b 0

echo("%trimmed%" | findstr /r "^[A-Za-z_][A-Za-z0-9_]*=.*" >nul
if errorlevel 1 (
    echo ERROR: malformed .env entry: %line%
    exit /b 1
)

for /f "tokens=1,* delims==" %%A in ("%trimmed%") do set "%%A=%%B"
exit /b 0
