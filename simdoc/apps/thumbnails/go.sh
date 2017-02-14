#!/bin/sh

rm -rf thumbnails
java -jar target/thumbnails-1.0-SNAPSHOT.jar "$@"
