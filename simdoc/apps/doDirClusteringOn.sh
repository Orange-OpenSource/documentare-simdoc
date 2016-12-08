#!/bin/sh

ABS_DIR=`pwd`/$1
(cd Ncd && ./go.sh -d1 $ABS_DIR) && \
(cd PrepClustering && ./go.sh -f ../Ncd/ncd_regular_files_model.json.gz) && \
(cd SimClustering/ && ./go.sh -i ../PrepClustering/prep_clustering_ready.json.gz -scut -ccut) && \
(cd Graph && ./go.sh -i ../SimClustering/sc_graph_input.json.gz -d ../Ncd/thumbnails && ./show.sh)
