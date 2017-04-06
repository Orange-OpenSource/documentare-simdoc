package com.orange.documentare.core.comp.clustering.graph.clusters;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.core.comp.clustering.graph.scissors.clusterlongedges.ClusterLongEdgesScissor;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor(suppressConstructorProperties = true)
@Slf4j
public class ClusterTreatments {
  private final ClusteringGraph clusteringGraph;
  private final ClusteringParameters clusteringParameters;

  public void cutLongestVertices() {
    ClusterLongEdgesScissor scissor = new ClusterLongEdgesScissor(clusteringGraph, clusteringGraph.getClusters().values(), clusteringParameters.ccutPercentile);
    int edgesCutInGraph = scissor.cut();
    log.info("Scalpel in clusters, {} edges cut", edgesCutInGraph);
  }

  public void updateClusterCenter() {
    ClusterCenter clusterCenter = new ClusterCenter(clusteringGraph.getItems(), clusteringGraph.getClusters().values());
    clusterCenter.updateCenter();
  }
}
