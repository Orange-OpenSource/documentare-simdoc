VERSION=1.0

build:
	(cd simdoc/core/java/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install -DskipTests=true) && \
	(cd simdoc/simdoc-server/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install -DskipTests=true)	

install:
	[ -d usr/lib/simdoc-server ] || mkdir -p usr/lib/simdoc-server
	cp simdoc/simdoc-server/target/simdoc-server-1.0.0-SNAPSHOT.jar usr/lib/simdoc-server
	cp simdoc/apps/ncd/target/ncd-1.0-SNAPSHOT.jar usr/bin
