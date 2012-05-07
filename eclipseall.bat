@echo off

rem go to current dir
cd /d %~dp0

rem set eclipse workspace location
set workspace=D:\tale\workspace\java\2012


rem for every sub dir, call mvn eclipse ...
for /f %%i in ('dir /B /A:D-H') do call :eclipse %%i

echo mvn eclipse completed
pause
goto :end


rem actural function of mvn eclipse
:eclipse
echo start mvn eclipse in %1
cd %1
IF EXIST "pom.xml" (
    cmd /c mvn eclipse:eclipse -Dwtpversion=2.0 -Declipse.ajdtVersion=none -Declipse.workspace=%workspace% -DdownloadSources=true
) ELSE (
    echo "no pom.xml in %1"
)
cd ..

:end
