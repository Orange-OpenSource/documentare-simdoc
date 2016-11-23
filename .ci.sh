#!/bin/sh

TAG=`git describe`

(cd simdoc/core/java/libs && ./mvnw clean install) && \
(cd simdoc/apps/pc/ && ./compileAll.sh) && \
(cd simdoc/apps/pc/ && ./refIntegrationTest.sh) && \
cp simdoc/apps/pc/LineDetection/build/libs/*.jar LineDetection-$TAG.jar && \
cp simdoc/apps/pc/Ncd/build/libs/*.jar Ncd-$TAG.jar && \
cp simdoc/apps/pc/PrepClustering/build/libs/*.jar PrepClustering-$TAG.jar && \
cp simdoc/apps/pc/SimClustering/build/libs/*.jar SimClustering-$TAG.jar && \
cp simdoc/apps/pc/Graph/build/libs/*.jar Graph-$TAG.jar && \
cp simdoc/apps/pc/Multisets/build/libs/*.jar Multisets-$TAG.jar && \
cp simdoc/apps/pc/Ocr/build/libs/*.jar Ocr-$TAG.jar && \
&& \
tar cvjf documentare-simdoc.tar.bz2 *.jar
echo && \
echo OK
