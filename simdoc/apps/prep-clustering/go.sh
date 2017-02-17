#!/bin/sh

rm -f *.csv* *.json*
java -Xmx5G -jar target/prep-clustering-1.0-SNAPSHOT.jar $1 $2 $3 $4 $5 $6
