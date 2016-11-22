#!/bin/sh

echo Build simdoc library && \
(cd core/java/libs && ./mvnw clean install) && \
echo Build simdoc tools && \
(cd apps/pc/ && ./compileAll.sh)
