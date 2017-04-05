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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/** Based on a given graph, check Area and Q computation */
public class ClusteringGraphBuilderQAreaTest {

  private static float[] EXPECTED_AREA = { 58.225315f, 58.225315f, 110.244896f, 58.225315f };
  private static float[] EXPECTED_Q = { 0.30491066f, 0.30491066f, 0.26493204f, 0.30491066f };

  @Getter
  @Setter
  @RequiredArgsConstructor
  class Item implements ClusteringItem {
    private final String humanReadableId;
    private Float nearestTriangleArea;
    private Integer clusterId;
    private Boolean clusterCenter;
    private NearestItem[] nearestItems;
    private byte[] bytes;

    private TriangleVertices triangleVertices;

    @Override
    public void setClusterCenter(boolean isCenter) {
      clusterCenter = isCenter;
    }

    @Override
    public boolean triangleVerticesAvailable() {
      return triangleVertices != null;
    }
  }

  @Test
  public void compute_graph_with_correct_area_and_q() {
    // given
    ClusteringItem[] clusteringItems = getClusteringItemsWithVertices();
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = ClusteringParameters.builder().acut().qcut().build();

    // do
    ClusteringGraph clusteringGraph = clusteringGraphBuilder.build(clusteringItems, parameters);

    // then
    for (int i = 0; i < clusteringItems.length; i++) {
      GraphItem graphItem = clusteringGraph.getItems().get(i);
      Assert.assertEquals(EXPECTED_AREA[i], graphItem.getArea(), 0.01f);
    }
    for (int i = 0; i < clusteringItems.length; i++) {
      GraphItem graphItem = clusteringGraph.getItems().get(i);
      Assert.assertEquals(EXPECTED_Q[i], graphItem.getQ(), 0.01f);
    }
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
