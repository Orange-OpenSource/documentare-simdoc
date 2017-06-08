#!/bin/sh

# to avoid issues with character encoding and filesystems
export LANG=C.UTF-8

TAG=`git describe`
echo git tag is: $TAG

(cd simdoc && ./prep-simdoc.sh) && \
(cd simdoc/simdoc-server && ./mvnw clean install) && \
(cd simdoc/apps && ./refIntegrationTest.sh) && \
cp simdoc/simdoc-server/target/simdoc-server-1.0.0-SNAPSHOT.jar simdoc-server-$TAG.jar && \
cp simdoc/apps/line-detection/target/line*.jar line-detection-$TAG.jar && \
cp simdoc/apps/prep-data/target/prep*.jar prep-data-$TAG.jar && \
cp simdoc/apps/ncd/target/ncd*.jar ncd-$TAG.jar && \
cp simdoc/apps/ncd-remote/target/ncd*.jar ncd-remote-$TAG.jar && \
cp simdoc/apps/prep-clustering/target/prep*.jar prep-clustering-$TAG.jar && \
cp simdoc/apps/similarity-clustering/target/sim*.jar similarity-clustering-$TAG.jar && \
cp simdoc/apps/graph/target/graph*.jar graph-$TAG.jar && \
cp simdoc/apps/base64/target/base64*.jar base64-$TAG.jar && \
cp simdoc/simdoc-server/target/simdoc-server*.jar simdoc-server-$TAG.jar && \
cp simdoc-server-$TAG.jar simdoc/simdoc-server && \
echo Create tarball && \
tar cvjf documentare-simdoc-$TAG.tar.bz2 *.jar && \
echo && \
echo OK && exit 0

echo [FAILED] && exit 1
