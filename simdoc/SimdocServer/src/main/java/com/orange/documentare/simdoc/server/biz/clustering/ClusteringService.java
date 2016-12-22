package com.orange.documentare.simdoc.server.biz.clustering;

import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;

interface ClusteringService {
  ClusteringResult build(String inputDirectory, String outputDirectory, ClusteringParameters clusteringParameters, boolean debug);
}
