#!/bin/sh

ABS_IMG=`pwd`/$1
(cd line-detection/ && ./go.sh $ABS_IMG && rm -f ld_out/*.raw) && \
(cd prep-data && ./go.sh -d ../line-detection/ld_out -bytes) && \
(cd ncd && ./go.sh -j1 ../prep-data/bytes-data.json) && \
(cd prep-clustering && ./go.sh -json ../ncd/ncd_regular_files_model.json.gz -writeCSV) && \
(cd similarity-clustering/ && ./go.sh -json ../prep-clustering/prep_clustering_ready.json.gz -acut -qcut -scut -ccut) && \
(cd graph && ./go.sh -map ../prep-data/metadata.json -json ../similarity-clustering/sc_graph_input.json.gz && ./show.sh) && \
echo && \
echo OK && \
echo
