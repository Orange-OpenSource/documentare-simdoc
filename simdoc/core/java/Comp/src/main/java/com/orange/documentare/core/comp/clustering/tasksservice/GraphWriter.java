package com.orange.documentare.core.comp.clustering.tasksservice;

import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;

public interface GraphWriter {
  void write(String outputGraphFilename, ClusteringGraph clusteringGraph, DigitalTypes digitalTypes);
}
