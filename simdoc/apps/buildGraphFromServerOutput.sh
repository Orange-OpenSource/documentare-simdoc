#!/bin/sh
(cd Thumbnails && ./go.sh -d $1/safe-input-dir) && \
(cd Graph && ./go.sh -d ../Thumbnails/thumbnails -json $1/clustering-graph.json.gz -map $1 && ./show.sh) && \
echo OK
