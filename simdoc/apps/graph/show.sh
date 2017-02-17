#!/bin/sh

/opt/local/bin/sfdp  graph.dot | /opt/local/bin/gvmap -e | /opt/local/bin/neato -Ecolor="#55555522" -n2 -Tpdf > graph.pdf
