#!/bin/sh

rm -rf sc_out *.csv* *.json* sc_multiset*
java -jar build/libs/SimClustering-all.jar $1 $2 $3 $4 $5 $6
