# This Batch file is used to  all the environment files necessary to 
# run the eCorp, by Altaprise, Inc.  This batch file should be
# used on unix platforms only.

# ###########################################
# ####   THESE TO YOUR UNIX ENV SETINGS
# ###########################################
dai_home=/home/daimgr/release
java_home=/home/daimgr/jdk1.2.2
# ###########################################


# Inprise Classpaths
classpath=$dai_home/jbcl3.0.jar:$dai_home/jbcl3.0-res.jar:$classpath

# DAI Classpaths
classpath=$dai_home/lib/dai.jar:$classpath
classpath=$dai_home/lib/daiBeans.jar:$classpath

# JSDK Classpaths
classpath=$dai_home/lib/servlet.jar:$classpath

# ProtoView Classpaths
classpath=$dai_home/lib/pvxAll.jar:$classpath

# InterClient JDBC Driver
classpath=$dai_home/lib/interclient.jar:$classpath

# XML Classpaths
classpath=$dai_home/lib/xml.jar:$classpath