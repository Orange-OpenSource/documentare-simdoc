#!/bin/sh

ABS_DIR=`pwd`/$1
(cd prep-data && ./go.sh  -d $ABS_DIR) && \
(cd ncd && ./go.sh -d1 ../prep-data/safe-working-dir) && \
(cd prep-clustering && ./go.sh -json ../ncd/ncd_regular_files_model.json.gz) && \
(cd similarity-clustering && ./go.sh -json ../prep-clustering/prep_clustering_ready.json.gz -acut -qcut -scut -ccut) && \
(cd thumbnails && ./go.sh -d ../prep-data/safe-working-dir) && \
(cd graph && ./go.sh -map ../prep-data/metaclass.json -json ../similarity-clustering/sc_graph_input.json.gz -d ../thumbnails/thumbnails && ./show.sh)
