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
set JAVA_OPTS=-Xms64M -Xmx128M
REM ###########################################


set CLASSPATH=

REM DAI Jars
set CLASSPATH=%DAI_HOME%\lib\clientupdate.jar;%CLASSPATH%

REM Apache Net Classpaths
set CLASSPATH=%DAI_HOME%\lib\commons-net-1.3.0.jar;%CLASSPATH%
set CLASSPATH=%DAI_HOME%\lib\jakarta-oro-2.0.8.jar;%CLASSPATH%

%JAVA_HOME%\bin\java.exe %JAVA_OPTS% -DDAI_HOME=%dai_home% com.altaprise.clientupdate.ClientUpdate %1 %2 %3

@endlocal

exit