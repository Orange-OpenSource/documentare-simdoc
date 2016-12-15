#!/bin/sh

ABS_IMG=`pwd`/$1
(cd LineDetection/ && ./go.sh $ABS_IMG) && (cd Ncd && ./go.sh -simdoc ../LineDetection/ld_segmentation.json.gz) && (cd SimClustering/ && ./go.sh -simdoc ../Ncd/ncd_simdoc_model_ready_for_clustering.json.gz -acut -qcut -scut -ccut) && (cd Graph && ./go.sh -json ../SimClustering/sc_graph_input.json.gz -d ../LineDetection/ld_out && ./show.sh)
