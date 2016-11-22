#!/bin/sh

(cd simdoc/core/java/libs && ./mvnw clean install) && \
(cd simdoc/apps/pc/ && ./compileAll.sh) && \
(cd simdoc/apps/pc/ && ./refIntegrationTest.sh) && \
echo OK
