#!/bin/sh

rm -rf thumbnails
java -jar target/Thumbnails-1.0-SNAPSHOT.jar "$@"
