# This Batch file is used to set all the environment files necessary to 
# run the eCorp, by Digital Artifacts, Inc.  This batch file should be
# used on unix platforms only.
###########################################
####  SET THESE TO YOUR WIN32 ENV SETTINGS
###########################################
export DAI_HOME="."
export JAVA_HOME=./jre
# export JAVA_OPTS=-Xms64M -Xmx256M
###########################################
# classpath=
# Inprise classpaths

# ProtoView classpath
export classpath=""
export classpath="./lib/pvxAll.jar:$classpath"

# InterClient JDBC Driver
export classpath="./lib/firebirdsql-full.jar:$classpath"
export classpath="./lib/interclient.jar:$classpath"

# Hypersonic JDBC Driver
export classpath="./lib/hsqldb.jar:$classpath"

# DAI Jars
export classpath="./lib/dai.jar:$classpath"
export classpath="./lib/daiBeans.jar:$classpath"

# Apache Net classpaths
export classpath="./lib/commons-net-1.3.0.jar:$classpath"
export classpath="./lib/jakarta-oro-2.0.8.jar:$classpath"
export classpath="./lib/jbcl3.0.jar:$classpath"
export classpath="./lib/jbcl3.0-res.jar:$classpath"

export PATH=$JAVA_HOME/bin:$DAI_HOME/lib:$PATH
echo $PATH
echo $classpath

java -cp $classpath -DDAI_HOME=. dai.client.clientAppRoot.AppRoot
