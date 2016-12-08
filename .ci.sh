#!/bin/sh

TAG=`git describe`

(cd simdoc/core/java/libs && ./mvnw clean install) && \
(cd simdoc/apps/pc/ && ./mvnw clean install) && \
(cd simdoc/apps/pc/ && ./refIntegrationTest.sh) && \
cp simdoc/apps/pc/LineDetection/target/Line*.jar LineDetection-$TAG.jar && \
cp simdoc/apps/pc/Ncd/target/Ncd*.jar Ncd-$TAG.jar && \
cp simdoc/apps/pc/PrepClustering/target/Prep*.jar PrepClustering-$TAG.jar && \
cp simdoc/apps/pc/SimClustering/target/Sim*.jar SimClustering-$TAG.jar && \
cp simdoc/apps/pc/Graph/target/Graph*.jar Graph-$TAG.jar && \
echo Create tarball && \
tar cvjf documentare-simdoc-$TAG.tar.bz2 *.jar && \
echo && \
echo OK && exit 0

echo [FAILED] && exit 1
