#!/bin/sh
(cd thumbnails && ./go.sh -d $1/safe-input-dir) && \
(cd graph && ./go.sh -d ../thumbnails/thumbnails -json $1/clustering-graph.json.gz -map $1 && ./show.sh) && \
echo OK
