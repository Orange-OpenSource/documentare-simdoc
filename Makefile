VERSION=`git describe`

# Quick build without tests
build-notest:
	#(cd simdoc/core/java/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install -DskipTests=true)
	#(cd simdoc/apps && mvn -Dmaven.repo.local=${HOME}/.m2/repository install -DskipTests=true)
	#(cd simdoc/simdoc-server/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install -DskipTests=true)

# Used to build the debian package
build:
	#(cd simdoc/core/java/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install)
	#(cd simdoc/apps && mvn -Dmaven.repo.local=${HOME}/.m2/repository install)
	#(cd simdoc/simdoc-server/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install)

# Build debian package
deb:
	debuild -uc -us -b

# Debian package install
install:
	rm -rf usr && mkdir -p usr/lib/simdoc-${VERSION} && (cd usr/lib && ln -s simdoc-${VERSION} simdoc)
	cp simdoc/simdoc-server/target/simdoc-server-*.jar usr/lib/simdoc/server.jar
	cp simdoc/apps/line-detection/target/line*.jar usr/lib/simdoc/line-detection.jar
	cp simdoc/apps/prep-data/target/prep*.jar usr/lib/simdoc/prep-data.jar
	cp simdoc/apps/ncd/target/ncd*.jar usr/lib/simdoc/ncd.jar
	cp simdoc/apps/ncd-remote/target/ncd*.jar usr/lib/simdoc/ncd-remote.jar
	cp simdoc/apps/prep-clustering/target/prep*.jar usr/lib/simdoc/prep-clustering.jar
	cp simdoc/apps/similarity-clustering/target/sim*.jar usr/lib/simdoc/similarity-clustering.jar
	cp simdoc/apps/graph/target/graph*.jar usr/lib/simdoc/graph.jar
