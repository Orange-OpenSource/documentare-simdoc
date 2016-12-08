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
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.clustering.graph.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor(suppressConstructorProperties = true)
@Log4j2
public class ClusterTreatments {
  private final ClusteringGraph clusteringGraph;
  private final ClusteringParameters clusteringParameters;
  private final ClusteringItem[] clusteringItems;

  public void cutLongestVertices() {
    ClusterLongEdgesScissor scissor = new ClusterLongEdgesScissor(clusteringGraph, clusteringGraph.getClusters().values(), clusteringParameters.getDistClusterThreshPercentile());
    int edgesCutInGraph = scissor.clean();
    log.info("Scalpel in clusters, {} edges cut", edgesCutInGraph);
  }

  public void updateClusterCenter() {
    ClusterCenter clusterCenter = new ClusterCenter(clusteringGraph.getItems(), clusteringGraph.getClusters().values());
    clusterCenter.updateCenter();
    updateClusteringItemsCenters();
  }

  void updateClusteringItemsCenters() {
    clusteringGraph.getItems().stream()
            .filter(graphItem -> graphItem.isClusterCenter())
            .mapToInt(graphItem -> graphItem.getVertex1Index())
            .forEach(index -> clusteringItems[index].setClusterCenter(true));
  }
}
