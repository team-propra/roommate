@echo off
setlocal EnableDelayedExpansion

if not exist ".env" (
    echo ERROR: .env not found.
    exit /b 1
)

for /f "usebackq tokens=* delims=" %%L in (".env") do (
    set "line=%%L"

    rem Skip blank lines
    if not "!line!"=="" (

        rem Skip comments
        if not "!line:~0,1!"=="#" (

            for /f "tokens=1,* delims==" %%A in ("!line!") do (
                set "%%A=%%B"
            )
        )
    )
)

echo Loaded .env

cmd /k