#!/bin/sh

ABS_IMG=`pwd`/$1
(cd line-detection/ && ./go.sh $ABS_IMG && rm -f ld_out/*.raw) && \
(cd prep-data && ./go.sh -d ../line-detection/ld_out) && \
(cd ncd && ./go.sh -d1 ../prep-data/safe-input-dir) && \
(cd prep-clustering && ./go.sh -json ../ncd/ncd_regular_files_model.json.gz -writeCSV) && \
(cd similarity-clustering/ && ./go.sh -json ../prep-clustering/prep_clustering_ready.json.gz -acut -qcut -scut -ccut) && \
(cd thumbnails && ./go.sh -d ../prep-data/safe-input-dir/) && \
(cd graph && ./go.sh -map ../prep-data -json ../similarity-clustering/sc_graph_input.json.gz -d ../thumbnails/thumbnails && ./show.sh) && \
echo && \
echo OK && \
echo
