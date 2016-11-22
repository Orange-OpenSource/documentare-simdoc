#!/bin/sh

rm -f *.csv* *.json*
java -Xmx5G -jar build/libs/PrepClustering-all.jar $1 $2 $3 $4 $5 $6
