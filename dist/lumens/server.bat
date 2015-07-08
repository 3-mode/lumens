REM echo off
set "MY_HOME=%CD%"
set "CLASSPATH=%MY_HOME%;%MY_HOME%\conf;%MY_HOME%\module\web;%MY_HOME%\server;%MY_HOME%\module\server\lib;%CLASSPATH%"
set "JAVA_HOME=C:\Sun\Java\jdk1.7.0_51_x64"
set "PATH=%JAVA_HOME%\bin;%PATH%"
java -classpath %CLASSPATH% -Dlumens.base=%MY_HOME% -Dlumens.lang=zh_CN -Xdebug -Xrunjdwp:transport=dt_socket,address=9009,server=y,suspend=n -Dlumens.port=9090 -Dlumens.log=file -jar module\server\lumens-server-1.0.jar
pause