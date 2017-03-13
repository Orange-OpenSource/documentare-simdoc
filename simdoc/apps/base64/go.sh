#!/bin/sh

rm -rf base64
java -Xmx5G -jar target/base64-1.0-SNAPSHOT.jar "$@"
