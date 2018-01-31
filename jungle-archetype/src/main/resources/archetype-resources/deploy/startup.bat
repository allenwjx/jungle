@ECHO OFF
setlocal

REM Copyright (c) 2001, 2016 LY.com Inc.
REM
REM Java application bat script.  Suitable for starting and stopping
REM wrapped Java applications on WINDOWS platforms.
REM

IF "%OS%"=="Windows_NT" goto nt
ECHO This script only works with NT-based versions of Windows.
goto :eof

:nt
REM 
REM Find the application home
REM 
REM %~dp0 is location of current script under NT
SET _REALPATH=%~dp0

ECHO Settings before change:
ECHO -----------------------
ECHO JAVA_HOME=%JAVA_HOME%
ECHO JAVA_OPTS=%JAVA_OPTS%
ECHO JPDA_OPTS=%JPDA_OPTS%
ECHO CLASSPATH=%CLASSPATH%
ECHO APP_ARGS=%APP_ARGS%

REM Configure remote Java debugging options here
REM Setting suspend=y will wait for you to connect before proceeding
SET JPDA_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005
SET JAVA_OPTS=-server -Xms1800m -Xmx1800m -Xmn680m -Xss256k -XX:PermSize=340m -XX:MaxPermSize=340m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:CMSFullGCsBeforeCompaction=5 -XX:+UseCMSCompactAtFullCollection -XX:+CMSClassUnloadingEnabled -XX:+DisableExplicitGC -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Dcom.sun.management.jmxremote.port=9981 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dfile.encoding=utf-8

REM Configure message server system properties
SET APP_ARGS=-Ddubbo.properties.file=/%_REALPATH%..\conf\dubbo.properties -Dlog4j.configurationFile=/%_REALPATH%..\conf\log4j.xml

REM
REM Find lib directory
REM
SET APP_CORE=%_REALPATH%..\core\*
SET APP_LIB=%_REALPATH%..\lib\*
SET APP_CFG=%_REALPATH%..\conf\
SET CLASSPATH=%CLASSPATH%;%APP_CORE%;%APP_LIB%;%APP_CFG%
IF DEFINED JAVA_HOME SET PATH="%JAVA_HOME%\bin";%PATH%

ECHO.
ECHO Settings after change:
ECHO ----------------------
ECHO JAVA_HOME=%JAVA_HOME%
ECHO JAVA_OPTS=%JAVA_OPTS%
ECHO JPDA_OPTS=%JPDA_OPTS%
ECHO CLASSPATH=%CLASSPATH%
ECHO APP_ARGS=%APP_ARGS%

:validate
rem Find the requested command.
for /F %%v in ('echo %1^|findstr "^console$ ^start$ ^stop$ ^restart$ "') do call :exec SET COMMAND=%%v
if "%COMMAND%" == "" (
    rem ###############################################################
    rem Customized for Jungle Container Server
    rem ###############################################################
    echo Running in console/foreground mode by default, use Ctrl-C to exit...
    set COMMAND=:console
    if "%1" == "-debug" (
        SET DEBUG=-debug
    )
    rem pause
    rem goto :eof
    rem ###############################################################
) else (
    SET DEBUG=%2
    shift
)

:run
rem ###############################################################
rem
rem Run Jungle container server...
rem
call :%COMMAND%
if errorlevel 1 pause
goto :eof
rem ###############################################################
rem Customized for Jungle Container Server
rem ###############################################################

:console
if "%DEBUG%" == "-debug" (
	%JAVA_HOME%\bin\java %JAVA_OPTS% %JPDA_OPTS% %APP_ARGS% -cp %CLASSPATH% com.zeh.jungle.core.boot.DefaultBootstrap
) else (
    %JAVA_HOME%\bin\java %JAVA_OPTS% %APP_ARGS% -cp %CLASSPATH% com.zeh.jungle.core.boot.DefaultBootstrap
)
goto :eof

:start
if "%DEBUG%" == "-debug" (
	start "" /MIN %JAVA_HOME%\bin\java %JAVA_OPTS% %JPDA_OPTS% %APP_ARGS% -cp %CLASSPATH% com.zeh.jungle.core.boot.DefaultBootstrap
) else (
    start "" /MIN %JAVA_HOME%\bin\java %JAVA_OPTS% %APP_ARGS% -cp %CLASSPATH% com.zeh.jungle.core.boot.DefaultBootstrap
)
goto :eof

:stop
%JAVA_HOME%\bin\java %JAVA_OPTS% -cp %CLASSPATH% com.zeh.jungle.core.boot.DefaultBootstrap
goto :eof

:restart
call :stop
call :start
goto :eof

:exec
%*
goto :eof