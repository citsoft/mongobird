#!/bin/sh
MIGRATION_BATCH_ID="sh/InstallForTetradRrd"
BATCH_PID=`ps -ef|grep $MIGRATION_BATCH_ID |grep -v grep|awk '{print $2}'`

if [ "$BATCH_PID" != "" ]
then
        echo "$MIGRATION_BATCH_ID - PID $BATCH_PID is already running."
else
        nohup ./InstallForTetradRrd.sh > /dev/null &
        echo "$MIGRATION_BATCH_ID is started"
fi
