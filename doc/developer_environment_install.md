This document presents how to setup a development workstation (debian/ubuntu) to compile/test/package the simdoc components

# Graphviz installation
Please follow the installation instructions provided in the `Install.md` document (`doc/servers` subdirectory): "Download & Install graphviz debian package"

# Tools to build and test the software components

First install the following dependencies on your debian/ubuntu system: `sudo apt-get install git make maven openjdk-8-jdk-headless devscripts libgvc6 libopencv-core2.4v5 libopencv2.4-java imagemagick`

NB: on ubuntu trusty, replace `libopencv-core2.4v5` by `libopencv-core2.4`

# How to build & package

Clone the repository : `git clone https://github.com/Orange-OpenSource/documentare-simdoc.git`

Setup the proxy configuration for maven if needed: `export MAVEN_OPTS="-Dhttp.proxyHost=proxy -Dhttp.proxyPort=8080 -Dhttps.proxyHost=proxy -Dhttps.proxyPort=8080`

Complete build as in the continuous integration: `./.ci.sh`
=> that is tha way to check that your installation is complete and works correctly

Quick build without tests: `make build-notest`
Build with tests: `make build-tests`
Build the servers debian package: `make deb`
Start the simdoc library integration test: `make integration-test`

# Docker installation

Do not use the docker package provided by your distribution. Follow installation instructions here: https://docs.docker.com/engine/installation/linux/docker-ce/debian/

If you use docker behind a proxy, you will need to configure it: https://docs.docker.com/engine/admin/systemd/#httphttps-proxy
