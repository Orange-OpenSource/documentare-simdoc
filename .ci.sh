#!/bin/sh

# to avoid issues with character encoding and filesystems
export LANG=C.UTF-8

TAG=`git describe`
echo git tag is: $TAG

make build-tests integration-test deb && \
echo Create tar && \
make tar && \
echo && \
echo OK && exit 0

echo [FAILED] && exit 1
