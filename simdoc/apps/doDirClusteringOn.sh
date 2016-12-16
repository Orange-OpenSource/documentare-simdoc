#!/bin/sh

ABS_DIR=`pwd`/$1
(cd Ncd && ./go.sh -d1 $ABS_DIR) && \
(cd PrepClustering && ./go.sh -json ../Ncd/ncd_regular_files_model.json.gz) && \
(cd SimClustering/ && ./go.sh -json ../PrepClustering/prep_clustering_ready.json.gz -acut -qcut -scut -ccut) && \
(cd Graph && ./go.sh -json ../SimClustering/sc_graph_input.json.gz -d ../Ncd/thumbnails && ./show.sh)
