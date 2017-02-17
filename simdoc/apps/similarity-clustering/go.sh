#!/bin/sh

rm -rf sc_out *.csv* *.json* sc_multiset*
java -jar target/similarity-clustering-1.0-SNAPSHOT.jar "$@"
