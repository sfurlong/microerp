rem Set the Runtime ENV variables.
call setrunenv.bat

rem Stop the Database
java -jar lib/hsqldb.jar --rcfile demo/sqltool.rc --sql shutdown localhost-sa

rem Stop the Web Server
java -Djetty.home=./jetty -jar jetty/stop.jar
