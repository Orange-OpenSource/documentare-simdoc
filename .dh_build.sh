#!/bin/sh

debuild -us -uc -b --lintian-opts -i
#dpkg-buildpackage -us -uc -b
