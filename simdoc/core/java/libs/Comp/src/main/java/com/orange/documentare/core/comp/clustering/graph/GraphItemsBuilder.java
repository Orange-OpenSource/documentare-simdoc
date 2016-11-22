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
        new TriangleVertices(item, items);

    // +1 to avoid null distance, which would disturb area and Q computation
    int edge12 = triangleVertices.getEdge12() + 1;
    int edge23 = triangleVertices.getEdge23() + 1;
    int edge31 = triangleVertices.getEdge13() + 1;

    return getGraphItemWith(itemIndex, triangleVertices.getVertex2(), triangleVertices.getVertex3(), edge12, edge23, edge31);
  }

  private GraphItem getGraphItemWith(int itemIndex, NearestItem vertex2, NearestItem vertex3, int edge12, int edge23, int edge31) {
    GraphItem graphItem = new GraphItem();
    ClusteringItem clusteringItem = items[itemIndex];
    graphItem.setVertexName(clusteringItem.getHumanReadableId());
    graphItem.setVertex1(clusteringItem);
    graphItem.setVertex1Index(itemIndex);
    graphItem.setVertex2(getClusteringItemFrom(vertex2));
    graphItem.setVertex3(getClusteringItemFrom(vertex3));
    graphItem.setEdgesLength(new int[]{edge12, edge23, edge31});
    float area = Heron.get(edge12, edge23, edge31);
    graphItem.setArea(area);
    graphItem.setQ(Equilaterality.get(area, edge12, edge23, edge31));
    return graphItem;
  }

  private NearestItem getVertex3(int itemIndex, NearestItem vertex2) {
    NearestItem[] nearestItemsVertex2 = items[vertex2.getIndex()].getNearestItems();
    NearestItem NearestItemOfVertex2 = nearestItemsVertex2[1];
    return NearestItemOfVertex2.getIndex() == itemIndex ?  nearestItemsVertex2[2] : NearestItemOfVertex2;
  }

  private int getDistanceTo(NearestItem[] nearestItems, int itemIndex) {
    for (NearestItem item : nearestItems) {
      if (item.getIndex() == itemIndex) {
        return item.getDistance();
      }
    }
    throw new IllegalStateException("Failed to find searchVertex2 item");
  }

  private ClusteringItem getClusteringItemFrom(NearestItem nearestItem) {
    for (int i = 0; i < items.length; i++) {
      if (i == nearestItem.getIndex()) {
        return items[i];
      }
    }
    throw new IllegalStateException("Failed to find searchVertex2 item");
  }

  private void updateVerticesIndex() {
    List<ClusteringItem> itemsList = Arrays.asList(items);
    for (GraphItem graphItem : graphItems) {
      graphItem.setVertex2Index(itemsList.indexOf(graphItem.getVertex2()));
      graphItem.setVertex3Index(itemsList.indexOf(graphItem.getVertex3()));
    }
  }
}
