#!/bin/sh

TMP=/tmp/a
TMP_SORT=/tmp/a_sort
rm -f $TMP
./mvnw license:add-third-party && \
for i in `find . -name THIRD-PARTY.txt`; do cat $i >> $TMP; done && \
sort $TMP > $TMP_SORT && \
uniq $TMP_SORT | grep -v "Lists of " | grep -v "com.orange.documentare.core" > 3RDPARTY.TXT
