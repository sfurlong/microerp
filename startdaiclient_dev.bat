rem echo off

rem Start the Database
start /MIN java -cp lib/hsqldb.jar org.hsqldb.Server -database demo\demodb.hsqldb -port 9001

rem Start the Web Server
start /MIN java -DDAI_HOME=%dai_home% -Djetty.home=%webserver_home% -jar %webserver_home%\start.jar

rem Start the UI
java -Xms64M -Xmx128M -DDAI_HOME=%dai_home% dai.client.clientAppRoot.AppRoot %1 %2
