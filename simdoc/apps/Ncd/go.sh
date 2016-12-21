#!/bin/sh

rm -f *.csv* *.json*
java -Xmx5G -jar target/Ncd-1.0-SNAPSHOT.jar "$@"
