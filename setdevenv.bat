@echo off
REM This Batch file is used to set all the environment files necessary to 
REM run the eCorp, by Digital Artifacts, Inc.  This batch file should be
REM used on Win32 platforms only.

REM ###########################################
REM ####  SET THESE TO YOUR WIN32 ENV SETTINGS
REM ###########################################
set DAI_HOME=c:\github\microerp
set JAVA_HOME=D:\oracle\Java\jdk1.7.0_40
set ANT_HOME=D:\oracle\Middleware11117\jdeveloper\ant
set WEBSERVER_HOME=%DAI_HOME%\jetty

set CLASSPATH=

REM Inprise Classpaths
set CLASSPATH=%DAI_HOME%\lib\jbcl3.0.jar;%CLASSPATH%
set CLASSPATH=%DAI_HOME%\lib\jbcl3.0-res.jar;%CLASSPATH%

REM DAI Classpaths
set CLASSPATH=%DAI_HOME%\classes;%CLASSPATH%

REM JDK Extension Classpaths
set CLASSPATH=%DAI_HOME%\lib\servlet.jar;%CLASSPATH%

REM ProtoView Classpaths
set CLASSPATH=%DAI_HOME%\lib\pvxAll.jar;%CLASSPATH%

REM Firebird JDBC Driver
set CLASSPATH=%DAI_HOME%\lib\firebirdsql-full.jar;%CLASSPATH%

REM InterClient JDBC Driver
set CLASSPATH=%DAI_HOME%\lib\interclient.jar;%CLASSPATH%

REM Hypersonic JDBC Driver
set CLASSPATH=%DAI_HOME%\lib\hsqldb.jar;%CLASSPATH%

REM XML Classpaths
set CLASSPATH=%DAI_HOME%\lib\xml.jar;%CLASSPATH%

REM Apache Net Classpaths
set CLASSPATH=%DAI_HOME%\lib\commons-net-1.3.0.jar;%CLASSPATH%
set CLASSPATH=%DAI_HOME%\lib\jakarta-oro-2.0.8.jar;%CLASSPATH%

REM Jasper Classpaths
set CLASSPATH=%DAI_HOME%\lib\jasperreports-1.2.8.jar;%CLASSPATH%


set path=%JAVA_HOME%\bin;%DAI_HOME%\lib;%path%
set path=%ANT_HOME%\bin;%PATH%

cd %DAI_HOME%


echo Completed Environment Setup