REM echo off
set "MY_HOME=%CD%"
set "CLASSPATH=%MY_HOME%\conf;%MY_HOME%\module\web;%MY_HOME%\server;%MY_HOME%\module\server\lib;%CLASSPATH%"
set "JAVA_HOME=C:\Sun\Java\jdk1.7.0_51_x64"
set "PATH=%JAVA_HOME%\bin;%PATH%"
java -classpath %CLASSPATH% -Dlumens.base=%MY_HOME% -Dlumens.port=9090 -Dlog4j.configurationFile=%MY_HOME%/conf/log4j2.xml -jar module\server\lumens-server-1.0.jar
pause