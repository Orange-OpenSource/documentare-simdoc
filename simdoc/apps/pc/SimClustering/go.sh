#!/bin/sh

rm -rf sc_out *.csv* *.json* sc_multiset*
java -jar target/SimClustering-1.0-SNAPSHOT.jar $1 $2 $3 $4 $5 $6
