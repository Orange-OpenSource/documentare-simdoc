VERSION=`git describe`

# Quick build without tests
build-notest:
	(cd simdoc/core/java/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install -DskipTests=true)
	(cd simdoc/apps && mvn -Dmaven.repo.local=${HOME}/.m2/repository install -DskipTests=true)
	(cd simdoc/simdoc-server/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install -DskipTests=true)	

# Used to build the debian package
build:
	(cd simdoc/core/java/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install)
	(cd simdoc/apps && mvn -Dmaven.repo.local=${HOME}/.m2/repository install)
	(cd simdoc/simdoc-server/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install)	

# Debian package install
install:
	mkdir -p usr/lib
	(cd usr/lib && \
	 mkdir simdoc-server && \
	 mkdir simdoc-ncd)
	cp simdoc/simdoc-server/target/simdoc-server-1.0.0-SNAPSHOT.jar usr/lib/simdoc-server/simdoc-server-$VERSION.jar
	cp simdoc/apps/ncd/target/ncd-1.0-SNAPSHOT.jar usr/lib/simdoc-ncd/simdoc-ncd-$VERSION.jar
