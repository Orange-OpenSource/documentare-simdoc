#!/bin/sh

# to avoid issues with character encoding and filesystems
export LANG=C.UTF-8

TAG=`git describe`
echo git tag is: $TAG

make build-notest build integration-test deb && \
echo Create tarball && \
tar cvjf documentare-simdoc-$TAG.tar.bz2 usr ../simdoc*.deb && \
echo && \
echo OK && exit 0

echo [FAILED] && exit 1
