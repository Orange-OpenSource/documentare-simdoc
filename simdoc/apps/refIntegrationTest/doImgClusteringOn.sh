#!/bin/sh

ABS_IMG=`pwd`/$1
(cd LineDetection/ && ./go.sh $ABS_IMG && rm -f ld_out/*.raw) && (cd PrepInputDir && ./go.sh -d ../LineDetection/ld_out/) && (cd Ncd && ./go.sh -d1 ../PrepInputDir/safe-input-dir) && (cd PrepClustering && ./go.sh -json ../Ncd/ncd_regular_files_model.json.gz -writeCSV) && (cd SimClustering/ && ./go.sh -json ../PrepClustering/prep_clustering_ready.json.gz -acut -qcut -scut -ccut) && (cd Graph && ./go.sh -json ../SimClustering/sc_graph_input.json.gz -d ../Ncd/thumbnails && ./show.sh)
