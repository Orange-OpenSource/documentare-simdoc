#!/bin/sh

if [ -z "$JAVA_MEM" ]; then
  echo "JAVA_MEM is empty"
else
  echo "JAVA_MEM is $JAVA_MEM"
  OPTIONS=-Xmx$JAVA_MEM
fi
echo "OPTIONS is $OPTIONS"

if [ -z "$SHARED_DIRECTORY" ]; then
  echo "SHARED_DIRECTORY is empty"
else
  echo "SHARED_DIRECTORY is $SHARED_DIRECTORY"
  OPTIONS="$OPTIONS --shared.directory.available=true --shared.directory.root.path=$SHARED_DIRECTORY"
fi

echo "OPTIONS is $OPTIONS"

java -jar *.jar $OPTIONS
