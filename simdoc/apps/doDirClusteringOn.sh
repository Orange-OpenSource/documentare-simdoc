#!/bin/sh

ABS_DIR=`pwd`/$1
(cd PrepInputDir && ./go.sh  -d $ABS_DIR) && \
(cd Ncd && ./go.sh -d1 ../PrepInputDir/safe-input-dir) && \
(cd PrepClustering && ./go.sh -json ../Ncd/ncd_regular_files_model.json.gz) && \
(cd SimClustering && ./go.sh -json ../PrepClustering/prep_clustering_ready.json.gz -acut -qcut -scut -ccut) && \
(cd Thumbnails && ./go.sh -d ../PrepInputDir/safe-input-dir) && \
(cd Graph && ./go.sh -map ../PrepInputDir -json ../SimClustering/sc_graph_input.json.gz -d ../Thumbnails/thumbnails && ./show.sh)
