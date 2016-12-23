package com.orange.documentare.simdoc.server.biz.clustering;

import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;

import java.io.File;
import java.io.IOException;

interface ClusteringService {
  ClusteringResult build(
    File inputDirectory, File outputDirectory, ClusteringParameters parameters, boolean debug) throws IOException;
}
