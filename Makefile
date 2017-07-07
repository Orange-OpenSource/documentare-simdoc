VERSION=`git describe`

# Quick build without tests
build-notest:
	(cd simdoc/core/java/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install -DskipTests=true)
	(cd simdoc/apps && mvn -Dmaven.repo.local=${HOME}/.m2/repository install -DskipTests=true)
	(cd simdoc/simdoc-server/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install -DskipTests=true)

build-tests:
	(cd simdoc/core/java/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install)
	(cd simdoc/apps && mvn -Dmaven.repo.local=${HOME}/.m2/repository install)
	(cd simdoc/simdoc-server/ && mvn -Dmaven.repo.local=${HOME}/.m2/repository install)

# Used to build debian package
# here we consider that artifacts are already built (to speed up continuous integration)
# so we do nothing here
build: 

integration-test:
	(cd simdoc/apps/ && ./refIntegrationTest.sh)

# Build debian package
deb:
	dch -v ${VERSION} "git update, version ${VERSION}"
	bash .dh_build.sh

# Debian package install
install:
	rm -rf usr && mkdir -p usr/share/java
	cp simdoc/simdoc-server/target/simdoc-server-*.jar usr/share/java/simdoc-server.jar
	rm -rf etc && mkdir -p etc/init.d && cp debian/server-init-d etc/init.d/simdoc-server

	# We only include server in deb package, here we copy jars to have it in the tarball
	cp simdoc/apps/line-detection/target/line*.jar line-detection.jar
	cp simdoc/apps/prep-data/target/prep*.jar prep-data.jar
	cp simdoc/apps/ncd/target/ncd*.jar ncd.jar
	cp simdoc/apps/ncd-remote/target/ncd*.jar ncd-remote.jar
	cp simdoc/apps/clustering-remote/target/clustering-remote*.jar clustering-remote.jar
	cp simdoc/apps/prep-clustering/target/prep*.jar prep-clustering.jar
	cp simdoc/apps/similarity-clustering/target/sim*.jar similarity-clustering.jar
	cp simdoc/apps/graph/target/graph*.jar graph.jar

clean:
	debclean
