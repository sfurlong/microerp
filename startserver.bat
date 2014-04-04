echo off

rem Set the Runtime ENV variables.
call setrunenv.bat

rem Start the Database
start /MIN java -cp lib/hsqldb.jar org.hsqldb.Server -database demo/demodb.hsqldb -port 9001

rem Start the Web Server
start /MIN java -DDAI_HOME=%dai_home% -Djetty.home=./jetty -jar jetty/start.jar
