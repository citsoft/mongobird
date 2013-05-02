#!/bin/sh

# You May Need To Change this to your Quartz installation root
workdir=`dirname $0`
workdir=`cd ${workdir} && pwd`
QUARTZ=${workdir}/../..

QUARTZ_CP=""

for jarfile in $QUARTZ/WEB-INF/lib/*.jar; do
  QUARTZ_CP=$QUARTZ_CP:$jarfile
done
