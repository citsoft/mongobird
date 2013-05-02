#!/bin/sh

# Change this to your JDK installation root
#
JAVA_HOME=/usr/local/jdk1.6.0_30

#JRE=$JAVA_HOME/jre
#JAVA=$JRE/bin/java
JAVA=${JAVA_HOME}/bin/java

workdir=`dirname $0`
workdir=`cd ${workdir} && pwd`
QUARTZ=${workdir}/../..

. ${QUARTZ}/sh/bin/buildcp.sh

LOGGING_PROPS="-Dlog4j.configuration=file:${workdir}/log4j.xml"

${JAVA} -cp ${QUARTZ_CP}:../../WEB-INF:../../WEB-INF/classes ${LOGGING_PROPS} \
-Xms256m -Xmx512m \
net.cit.tetrad.rrd.batch.TotalInfoScheduling


