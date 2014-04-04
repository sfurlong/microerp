@echo off
@setlocal
REM This Batch file is used to set all the environment files necessary to 
REM run the eCorp, by Digital Artifacts, Inc.  This batch file should be
REM used on Win32 platforms only.
REM ###########################################
REM ####  SET THESE TO YOUR WIN32 ENV SETTINGS
REM ###########################################
set DAI_HOME=.
set JAVA_HOME=%DAI_HOME%\jre
set JAVA_OPTS=-Xms64M -Xmx256M
REM ###########################################


set CLASSPATH=

REM Inprise Classpaths
set CLASSPATH=%DAI_HOME%\lib\jbcl3.0.jar;%CLASSPATH%
set CLASSPATH=%DAI_HOME%\lib\jbcl3.0-res.jar;%CLASSPATH%

REM ProtoView Classpaths
set CLASSPATH=%DAI_HOME%\lib\pvxAll.jar;%CLASSPATH%

REM InterClient JDBC Driver
set CLASSPATH=%DAI_HOME%\lib\firebirdsql-full.jar;%CLASSPATH%
set CLASSPATH=%DAI_HOME%\lib\interclient.jar;%CLASSPATH%

REM Hypersonic JDBC Driver
set CLASSPATH=%DAI_HOME%\lib\hsqldb.jar;%CLASSPATH%

REM DAI Jars
set CLASSPATH=%DAI_HOME%\lib\dai.jar;%CLASSPATH%
set CLASSPATH=%DAI_HOME%\lib\daiBeans.jar;%CLASSPATH%

REM Apache Net Classpaths
set CLASSPATH=%DAI_HOME%\lib\commons-net-1.3.0.jar;%CLASSPATH%
set CLASSPATH=%DAI_HOME%\lib\jakarta-oro-2.0.8.jar;%CLASSPATH%

set path=%JAVA_HOME%\bin;%DAI_HOME%\lib;%path%

rem start javaw %JAVA_OPTS% -DDAI_HOME=%dai_home% dai.client.clientAppRoot.AppRoot

java org.hsqldb.Server -database.0 demo/demodb.hsqldb -dbname.0 xdb

@endlocal

exit