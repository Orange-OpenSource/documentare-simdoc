package com.orange.documentare.simdoc.server.biz.clustering;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

@Slf4j
class ClusteringOutput {
  public final SimClusteringItem[] simClusteringItems;
  public final ClusteringGraph graph;

  ClusteringOutput(SimClusteringItem[] simClusteringItems, ClusteringGraph graph) {
    this.simClusteringItems = simClusteringItems;
    this.graph = graph;
    checkClusteringCoherence();
  }


  private void checkClusteringCoherence() {
    log.info("check clustering coherence");
    List<GraphItem> graphItems = graph.getItems();
    OptionalInt incoherent = IntStream.range(0, graphItems.size())
      .filter(i -> graphItems.get(i).getClusterId() != simClusteringItems[i].getClusterId())
      .findFirst();
    if (incoherent.isPresent()) {
      int i = incoherent.getAsInt();
      throw new IllegalStateException(
        String.format("cluster id not coherent: %d vs %d", graphItems.get(i).getClusterId(), simClusteringItems[i].getClusterId()));
    }
  }
}
