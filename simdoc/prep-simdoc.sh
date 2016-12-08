#!/bin/sh

echo Build simdoc library && \
(cd core/java && ./mvnw clean install) && \
echo Build simdoc tools && \
(cd apps && ./mvnw clean install)
