#!/bin/sh

ABS_IMG=`pwd`/$1
(cd LineDetection/ && ./go.sh $ABS_IMG) && (cd Ncd && ./go.sh -simDocJsonGz ../LineDetection/ld_segmentation.json.gz) && (cd SimClustering/ && ./go.sh -simDocJsonGz ../Ncd/ncd_simdoc_model_ready_for_clustering.json.gz -scut -ccut) && (cd Graph && ./go.sh -i ../SimClustering/sc_graph_input.json.gz -d ../LineDetection/ld_out && ./show.sh)
