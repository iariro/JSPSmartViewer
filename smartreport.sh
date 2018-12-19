#!/bin/sh

DATETIME=$( (echo 'dino2_sda' ; ./smartctl -a /dev/sda) | curl -X POST -H "Expect:" -H "Content-Type: text/plain" http://172.17.28.106:8080/kumagai/receivesmartctloutput --data-binary @-)
(echo 'dino2_sda' ; echo $DATETIME ; df) | curl -X POST -H "Expect:" -H "Content-Type: text/plain" http://172.17.28.106:8080/kumagai/receivedfoutput --data-binary @-
