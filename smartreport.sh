#!/bin/sh

DATETIME=$(/Users/kumagai/Documents/bin/smartmontools-6.5/smartctl -a /dev/disk1 | curl -X POST -H "Content-Type: text/plain" http://192.168.10.10:8080/kumagai/receivesmartctloutput --data-binary @-)
(echo $DATETIME ; df) | curl -X POST -H "Content-Type: text/plain" http://192.168.10.10:8080/kumagai/receivedfoutput --data-binary @-
