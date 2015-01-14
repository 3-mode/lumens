REM echo off
set "MY_HOME=%CD%"
set "CLASSPATH=%MY_HOME%\module\shared;%MY_HOME%\module\web;%MY_HOME%\server;%MY_HOME%\module\server\lib;%CLASSPATH%"
set "JAVA_HOME=C:\Sun\Java\jdk1.7.0_21"
set "PATH=%JAVA_HOME%\bin;%PATH%"
java -classpath %CLASSPATH% -Dlumens.base=%MY_HOME% -Dlumens.web=%MY_HOME%\module\web -Dlumens.port=9090 -jar module\server\lumens-server-1.0.jar
pause