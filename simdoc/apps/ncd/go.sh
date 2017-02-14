#!/bin/sh

rm -f *.csv* *.json*
java -Xmx5G -jar target/ncd-1.0-SNAPSHOT.jar "$@"
