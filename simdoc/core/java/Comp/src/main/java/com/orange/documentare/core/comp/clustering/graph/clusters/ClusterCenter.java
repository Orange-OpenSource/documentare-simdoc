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

import com.orange.documentare.core.model.ref.clustering.graph.GraphCluster;
import com.orange.documentare.core.model.ref.clustering.graph.GraphEdge;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(suppressConstructorProperties = true)
public class ClusterCenter {
  private final List<GraphItem> items;
  private final Collection<GraphCluster> clusters;

  public void updateCenter() {
    for (GraphCluster cluster : clusters) {
      updateCenter(cluster);
    }
  }

  private void updateCenter(GraphCluster cluster) {
    if (cluster.getItemIndices().size() == 1) {
      items.get(cluster.getItemIndices().get(0)).setClusterCenter(true);
    } else {
      updateCenterNonSingleton(cluster);
    }
  }

  private void updateCenterNonSingleton(GraphCluster cluster) {
    int maxEdgesCount = -1;
    int centerIndex = 0;
    Map<Integer, Integer> edgesCount = getEdgesCountFor(cluster);
    for (int index : cluster.getItemIndices()) {
      GraphItem item = items.get(index);
      item.setClusterCenter(false);
      int count = edgesCount.get(item.getVertex1Index());
      if (count > maxEdgesCount) {
        maxEdgesCount = count;
        centerIndex = index;
      }
    }
    items.get(centerIndex).setClusterCenter(true);
  }

  private Map<Integer, Integer> getEdgesCountFor(GraphCluster cluster) {
    Map<Integer, Integer> edgesCount = new HashMap<>();
    for (GraphEdge edge : cluster.getEdges()) {
      updateEdgesCountWith(edgesCount, edge);
    }
    return edgesCount;
  }

  private void updateEdgesCountWith(Map<Integer, Integer> edgesCount, GraphEdge edge) {
    int index1 = edge.getVertex1Index();
    int index2 = edge.getVertex2Index();
    Integer count1 = edgesCount.get(index1);
    Integer count2 = edgesCount.get(index2);
    count1 = count1 == null ? 1 : count1 + 1;
    count2 = count2 == null ? 1 : count2 + 1;
    edgesCount.put(index1, count1);
    edgesCount.put(index2, count2);
  }
}
