#!/bin/sh

sfdp graph.dot | gvmap -e | neato -Ecolor="#55555522" -n2 -Tpdf > graph.pdf
