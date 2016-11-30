package com.orange.documentare.core.comp.clustering.graph;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.geometry.Equilaterality;
import com.orange.documentare.core.comp.clustering.geometry.Heron;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.comp.TriangleVertices;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
class GraphItemsBuilder {
  private final ClusteringItem[] items;
  private final List<GraphItem> graphItems;
  private final int kNearestNeighboursThreshold;

  void initGraphItems() {
    for (int i = 0; i < items.length; i++) {
      GraphItem graphItem = getGraphItemFor(i);
      graphItems.add(graphItem);
    }
    updateVerticesIndex();
  }

  private GraphItem getGraphItemFor(int itemIndex) {
    ClusteringItem item = items[itemIndex];
    TriangleVertices triangleVertices = item.triangleVerticesAvailable() ?
        item.getTriangleVertices() :
        new TriangleVertices(item, items, kNearestNeighboursThreshold);

    boolean orphan = triangleVertices.isOrphan();
    GraphItem graphItem = buildGraphItem(itemIndex, orphan);
    if (!orphan) {
      addTriangleInfo(graphItem, triangleVertices);
    }
    return graphItem;
  }

  private GraphItem buildGraphItem(int itemIndex, boolean orphan) {
    GraphItem graphItem = new GraphItem();
    ClusteringItem clusteringItem = items[itemIndex];
    if (orphan) {
      graphItem.setOrphan(true);
    }
    graphItem.setVertexName(clusteringItem.getHumanReadableId());
    graphItem.setVertex1(clusteringItem);
    graphItem.setVertex1Index(itemIndex);
    return graphItem;
  }

  private void addTriangleInfo(GraphItem graphItem, TriangleVertices triangleVertices) {
    graphItem.setVertex2(getClusteringItemFrom(triangleVertices.getVertex2()));
    graphItem.setVertex3(getClusteringItemFrom(triangleVertices.getVertex3()));

    // +1 to avoid null distance, which would disturb area and Q computation
    int edge12 = triangleVertices.getEdge12() + 1;
    int edge23 = triangleVertices.getEdge23() + 1;
    int edge31 = triangleVertices.getEdge13() + 1;
    graphItem.setEdgesLength(new int[]{edge12, edge23, edge31});

    float area = Heron.get(edge12, edge23, edge31);
    graphItem.setArea(area);
    graphItem.setQ(Equilaterality.get(area, edge12, edge23, edge31));
  }

  private ClusteringItem getClusteringItemFrom(NearestItem nearestItem) {
    for (int i = 0; i < items.length; i++) {
      if (i == nearestItem.getIndex()) {
        return items[i];
      }
    }
    throw new IllegalStateException("Failed to find clustering item");
  }

  private void updateVerticesIndex() {
    List<ClusteringItem> itemsList = Arrays.asList(items);
    for (GraphItem graphItem : graphItems) {
      graphItem.setVertex2Index(itemsList.indexOf(graphItem.getVertex2()));
      graphItem.setVertex3Index(itemsList.indexOf(graphItem.getVertex3()));
    }
  }
}
