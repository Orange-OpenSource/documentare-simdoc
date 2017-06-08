#!/bin/sh

rm -f *.csv* *.json*
java -Xmx5G -jar target/ncd-remote-1.0-SNAPSHOT.jar "$@"
