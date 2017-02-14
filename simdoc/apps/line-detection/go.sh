#!/bin/sh

rm -rf *.json* ld_out *.png
java -jar target/line-detection-1.0-SNAPSHOT.jar $1
