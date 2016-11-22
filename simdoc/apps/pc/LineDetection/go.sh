#!/bin/sh 

rm -rf *.json* ld_out *.png
java -jar build/libs/LineDetection-all.jar $1
