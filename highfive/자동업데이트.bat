@echo off
setlocal

:: ========================================
:: �ڵ� ������Ʈ �ý��� ����
:: ========================================
echo ========================================
echo       [ �ڵ� ������Ʈ �ý��� ���� ]
echo ========================================
echo.

:: �ٿ�ε� �ּ� ����
set VERSION_FILE=https://raw.githubusercontent.com/sskim444/l2update/main/version.txt
set UPDATE_FILE=https://raw.githubusercontent.com/sskim444/l2update/main/update.zip

:: update ���� ������ ����
if not exist update (
    mkdir update
)

echo [1] ���� ���� Ȯ�� ��...

powershell -Command "(New-Object Net.WebClient).DownloadString('%VERSION_FILE%')" > update\_server_version.txt 2>update\error.log

if not exist update\_server_version.txt (
    echo [!] �������� version.txt �ٿ�ε� ����!
    type update\error.log
    goto end
)

set /p SERVER_VERSION=< update\_server_version.txt
del update\_server_version.txt
for /f %%A in ("%SERVER_VERSION%") do set "SERVER_VERSION=%%~A"

echo [2] ���� ���� Ȯ�� ��...

set "LOCAL_VERSION="
if exist update\version.txt (
    for /f %%v in (update\version.txt) do set "LOCAL_VERSION=%%v"
    for /f %%A in ("%LOCAL_VERSION%") do set "LOCAL_VERSION=%%~A"
)

if not defined LOCAL_VERSION (
    echo [!] ���� ������ �����ϴ�. ������Ʈ�� �����մϴ�.
    goto download
)

if "%LOCAL_VERSION%"=="%SERVER_VERSION%" (
    echo [?] �ֽ� �����Դϴ�. ������Ʈ���� �ʽ��ϴ�.
    goto end
)

echo [!] �� ���� �߰�! ������Ʈ�� �����մϴ�.
echo     ���� ����: %LOCAL_VERSION%
echo     ���� ����: %SERVER_VERSION%

:download
echo [3] update.zip �ٿ�ε� ��...
powershell -Command "(New-Object Net.WebClient).DownloadFile('%UPDATE_FILE%', 'update\update.zip')" 2>>update\error.log

if not exist update\update.zip (
    echo [X] update.zip �ٿ�ε� ����!
    type update\error.log
    goto end
)

echo [4] ���� ���� ��...
powershell -Command "Expand-Archive -Path 'update\update.zip' -DestinationPath '.' -Force" 2>>update\error.log

if errorlevel 1 (
    echo [X] ���� ���� ����!
    type update\error.log
    goto end
)

echo [5] ������Ʈ �Ϸ�! ���� ���� ���� ��...
echo %SERVER_VERSION% > update\version.txt
del update\update.zip

echo.
echo ----------------------------------------
echo    ���� ����: %SERVER_VERSION%
echo ----------------------------------------

:end
echo.
echo �۾��� ���ƽ��ϴ�. â�� �������� �ƹ� Ű�� ��������.
pause >nul
