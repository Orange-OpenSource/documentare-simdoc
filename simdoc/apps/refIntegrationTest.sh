#!/bin/sh

./refIntegrationTest/doImgClusteringOn.sh refIntegrationTest/anatomie_crop.png && \
gunzip similarity-clustering/sc_graph_input.json.gz && \
diff similarity-clustering/sc_graph_input.json refIntegrationTest/sc_graph_input.json && \
./refIntegrationTest/doSimDocImgClusteringOn.sh refIntegrationTest/anatomie_crop.png && \
gunzip similarity-clustering/sc_graph_input.json.gz && \
diff similarity-clustering/sc_graph_input.json refIntegrationTest/sc_simdoc_graph_input.json && \
gunzip similarity-clustering/sc_segmentation_ready_for_user_interface.json.gz && \
diff similarity-clustering/sc_segmentation_ready_for_user_interface.json refIntegrationTest/sc_segmentation_ready_for_user_interface.json && \
echo "Integration test OK" && \
exit 0

echo "[FAILED] Integration test" && exit 1
