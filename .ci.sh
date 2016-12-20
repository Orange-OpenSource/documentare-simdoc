#!/bin/sh

TAG=`git describe`

(cd simdoc && ./prep-simdoc.sh) && \
(cd simdoc/apps && ./refIntegrationTest.sh) && \
cp simdoc/apps/LineDetection/target/Line*.jar LineDetection-$TAG.jar && \
cp simdoc/apps/Ncd/target/Ncd*.jar Ncd-$TAG.jar && \
cp simdoc/apps/PrepClustering/target/Prep*.jar PrepClustering-$TAG.jar && \
cp simdoc/apps/SimClustering/target/Sim*.jar SimClustering-$TAG.jar && \
cp simdoc/apps/Graph/target/Graph*.jar Graph-$TAG.jar && \
echo Create tarball && \
tar cvjf documentare-simdoc-$TAG.tar.bz2 *.jar && \
echo && \
echo OK && exit 0

echo [FAILED] && exit 1
