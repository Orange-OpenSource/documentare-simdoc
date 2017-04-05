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

import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.comp.TriangleVertices;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ClusteringGraphBuilderScalpelTest {

  @Test
  public void compute_triangles_areas() {
    // given
    ClusteringItem[] clusteringItems = getClusteringItemsWithVertices();
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = ClusteringParameters.builder().scut().build();
    // do
    ClusteringGraph clusteringGraph = clusteringGraphBuilder.build(clusteringItems, parameters);

    // then
    List<GraphItem> graphItems = clusteringGraph.getItems();

    // TODO
  }

  private ClusteringItem[] getClusteringItemsWithVertices() {
    Item[] items = getClusteringItems();
    int knn = items.length;
    for (int i = 0; i < items.length; i++) {
      items[i].setTriangleVertices(new TriangleVertices(items[i], items, knn));
    }
    // Ensure we can only rely on vertices
    Arrays.asList(items).stream().forEach(item -> item.setNearestItems(null));
    return items;
  }

  private Item[] getClusteringItems() {
    Item[] items = getItems();
    initItems(items);
    return items;
  }

  private void initItems(Item[] items) {
    // TODO: build relevant initial graph for test

    items[0].setNearestItems(new NearestItem[]{ new NearestItem(0, 0), new NearestItem(1, 20), new NearestItem(2, 25), new NearestItem(3, 30)});
    items[1].setNearestItems(new NearestItem[]{ new NearestItem(1, 0), new NearestItem(3, 10), new NearestItem(0, 20), new NearestItem(2, 45)});
    items[2].setNearestItems(new NearestItem[]{ new NearestItem(2, 0), new NearestItem(0, 25), new NearestItem(1, 45), new NearestItem(3, 55)});
    items[3].setNearestItems(new NearestItem[]{ new NearestItem(3, 0), new NearestItem(1, 10), new NearestItem(0, 30), new NearestItem(2, 55)});
  }

  private Item[] getItems() {
    Item[] items = new Item[4];
    for (int i = 0; i < items.length; i++) {
      items[i] = new Item(String.valueOf(i));
    }
    return items;
  }
}
