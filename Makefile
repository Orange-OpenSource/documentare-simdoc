VERSION=`git describe`

# Quick build without tests
build-notest:
	(cd simdoc/core/java/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install -DskipTests=true)
	(cd simdoc/apps && mvn -Dmaven.repo.local=${HOME}/.m2/repository install -DskipTests=true)
	(cd simdoc/simdoc-server/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install -DskipTests=true)

# Used to build the debian package
build:
	#(cd simdoc/core/java/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install)
	#(cd simdoc/apps && mvn -Dmaven.repo.local=${HOME}/.m2/repository install)
	#(cd simdoc/simdoc-server/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install)

# Debian package install
install:
	mkdir -p usr/lib/simdoc
	(cd usr/lib/simdoc && \
	 mkdir -p graph line-detection ncd ncd-remote prep-clustering prep-data similarity-clustering server)
	cp simdoc/simdoc-server/target/simdoc-server-*.jar usr/lib/simdoc/server-${VERSION}.jar
	cp simdoc/apps/line-detection/target/line*.jar usr/lib/simdoc/line-detection-${VERSION}.jar
	cp simdoc/apps/prep-data/target/prep*.jar usr/lib/simdoc/prep-data-${VERSION}.jar
	cp simdoc/apps/ncd/target/ncd*.jar usr/lib/simdoc/ncd-${VERSION}.jar
	cp simdoc/apps/ncd-remote/target/ncd*.jar usr/lib/simdoc/ncd-remote-${VERSION}.jar
	cp simdoc/apps/prep-clustering/target/prep*.jar usr/lib/simdoc/prep-clustering-${VERSION}.jar
	cp simdoc/apps/similarity-clustering/target/sim*.jar usr/lib/simdoc/similarity-clustering-${VERSION}.jar
	cp simdoc/apps/graph/target/graph*.jar usr/lib/simdoc/graph-${VERSION}.jar