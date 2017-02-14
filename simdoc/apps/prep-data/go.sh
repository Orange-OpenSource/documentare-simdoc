#!/bin/sh

rm -rf *.csv* *.json* safe-input-dir
java -Xmx5G -jar target/PrepInputDir-1.0-SNAPSHOT.jar $1 $2 $3 $4 $5 $6
