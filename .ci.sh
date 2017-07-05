#!/bin/sh

# to avoid issues with character encoding and filesystems
export LANG=C.UTF-8

TAG=`git describe`
echo git tag is: $TAG

make build integration-test deb && \
echo Create tarball && \
tar cvf documentare-simdoc-$TAG.tar usr ../simdoc*.deb && \
echo && \
echo OK && exit 0

echo [FAILED] && exit 1
