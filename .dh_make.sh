#!/bin/sh

export DEBEMAIL=christophe.maldivi@orange.com
export DEBFULLNAME="Christophe Maldivi"

VERSION=`git describe`
PACKAGENAME=simdoc-server

dh_make -p ${PACKAGENAME}_${VERSION} -c gpl2 --native -s

