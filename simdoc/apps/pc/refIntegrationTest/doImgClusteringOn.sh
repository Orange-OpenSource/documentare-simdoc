#!/bin/sh

ABS_IMG=`pwd`/$1
(cd LineDetection/ && ./go.sh $ABS_IMG && rm -f ld_out/*.raw) && (cd Ncd && ./go.sh -file1 ../LineDetection/ld_out) && (cd PrepClustering && ./go.sh -tr -f ../Ncd/ncd_regular_files_model.json.gz) && (cd SimClustering/ && ./go.sh -i ../PrepClustering/prep_clustering_ready.json.gz -scut -ccut) && (cd Graph && ./go.sh -i ../SimClustering/sc_graph_input.json.gz -d ../Ncd/thumbnails && ./show.sh)
