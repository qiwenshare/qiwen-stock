#!/bin/bash

PID=$(ps -ef | grep app/stock-web |grep -v grep | awk '{ print $2 }')
if [ -z "$PID" ]
then
	echo Application is already stopped
else
	echo kill $PID
	kill $PID
fi

