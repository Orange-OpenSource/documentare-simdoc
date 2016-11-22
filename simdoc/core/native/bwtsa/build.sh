#!/bin/sh

(cd ../../java/libs/Comp && mvn install -DskipTests=true) && \
javah -verbose -jni -d include -classpath ../../java/libs/Comp/target/classes com.orange.documentare.core.comp.bwt.SaisBwt && \
\
echo &&\
echo "[JNI header] OK" && \
echo &&\
\
qmake libbwtsa.pro && make clean all && \
\
echo && \
echo "[JNI library] OK" && \
echo
