This document presents how to setup a development workstation (debian/ubuntu) to compile/test/package the simdoc components

# Graphviz installation
Please follow the installation instructions provided in the `Install.md` document (`doc/servers` subdirectory): "Download & Install graphviz debian package"

# Tools to build and test the software components

First install the following dependencies on your debian/ubuntu system: `sudo apt-get install git make maven openjdk-8-jdk-headless`

# How to build

Complete build as in the continuous integration: `./.ci.sh`
Quick build without tests: `make build-notest`
Build with tests: `build-tests`
