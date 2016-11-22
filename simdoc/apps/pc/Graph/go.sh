#!/bin/sh

rm -rf out *.csv
java -jar build/libs/Graph-all.jar $1 $2 $3 $4 $5 $6
