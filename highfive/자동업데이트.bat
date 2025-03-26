@echo off
setlocal

:: ========================================
:: 자동 업데이트 시스템 시작
:: ========================================
echo ========================================
echo       [ 자동 업데이트 시스템 시작 ]
echo ========================================
echo.

:: 다운로드 주소 설정
set VERSION_FILE=https://raw.githubusercontent.com/sskim444/l2update/main/version.txt
set UPDATE_FILE=https://raw.githubusercontent.com/sskim444/l2update/main/update.zip

:: update 폴더 없으면 생성
if not exist update (
    mkdir update
)

echo [1] 서버 버전 확인 중...

powershell -Command "(New-Object Net.WebClient).DownloadString('%VERSION_FILE%')" > update\_server_version.txt 2>update\error.log

if not exist update\_server_version.txt (
    echo [!] 서버에서 version.txt 다운로드 실패!
    type update\error.log
    goto end
)

set /p SERVER_VERSION=< update\_server_version.txt
del update\_server_version.txt
for /f %%A in ("%SERVER_VERSION%") do set "SERVER_VERSION=%%~A"

echo [2] 로컬 버전 확인 중...

set "LOCAL_VERSION="
if exist update\version.txt (
    for /f %%v in (update\version.txt) do set "LOCAL_VERSION=%%v"
    for /f %%A in ("%LOCAL_VERSION%") do set "LOCAL_VERSION=%%~A"
)

if not defined LOCAL_VERSION (
    echo [!] 로컬 버전이 없습니다. 업데이트를 진행합니다.
    goto download
)

if "%LOCAL_VERSION%"=="%SERVER_VERSION%" (
    echo [?] 최신 버전입니다. 업데이트하지 않습니다.
    goto end
)

echo [!] 새 버전 발견! 업데이트를 진행합니다.
echo     로컬 버전: %LOCAL_VERSION%
echo     서버 버전: %SERVER_VERSION%

:download
echo [3] update.zip 다운로드 중...
powershell -Command "(New-Object Net.WebClient).DownloadFile('%UPDATE_FILE%', 'update\update.zip')" 2>>update\error.log

if not exist update\update.zip (
    echo [X] update.zip 다운로드 실패!
    type update\error.log
    goto end
)

echo [4] 압축 해제 중...
powershell -Command "Expand-Archive -Path 'update\update.zip' -DestinationPath '.' -Force" 2>>update\error.log

if errorlevel 1 (
    echo [X] 압축 해제 실패!
    type update\error.log
    goto end
)

echo [5] 업데이트 완료! 버전 정보 갱신 중...
echo %SERVER_VERSION% > update\version.txt
del update\update.zip

echo.
echo ----------------------------------------
echo    현재 버전: %SERVER_VERSION%
echo ----------------------------------------

:end
echo.
echo 작업을 마쳤습니다. 창을 닫으려면 아무 키나 누르세요.
pause >nul
