#!/bin/sh
MIGRATION_BATCH_ID="sh/TotalInfoScheduling"
BATCH_PID=`ps -ef|grep $MIGRATION_BATCH_ID |grep -v grep|awk '{print $2}'`

if [ "$BATCH_PID" != "" ]
then
        kill $BATCH_PID > /dev/null
        echo "$MIGRATION_BATCH_ID - PID $BATCH_PID is stopped"
else
        echo "$MIGRATION_BATCH_ID already stopped"
fi
