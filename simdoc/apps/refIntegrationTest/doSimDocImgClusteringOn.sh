#!/bin/sh

ABS_IMG=`pwd`/$1
(cd line-detection/ && ./go.sh $ABS_IMG) && (cd ncd && ./go.sh -simdoc ../line-detection/ld_segmentation.json.gz) && (cd similarity-clustering/ && ./go.sh -simdoc ../ncd/ncd_simdoc_model_ready_for_clustering.json.gz -acut -qcut -scut -ccut) && (cd graph && ./go.sh -json ../similarity-clustering/sc_graph_input.json.gz -d ../line-detection/ld_out && ./show.sh)
