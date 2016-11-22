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

import com.orange.documentare.core.comp.clustering.graph.scissors.ClusteringParameters;
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

public class ClusteringGraphBuilderGetClusteringGraphTest {
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
  public void shouldComputeTrianglesAreas() {
    // given
    ClusteringItem[] clusteringItems = getClusteringItems();
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();

    // do
    ClusteringGraph clusteringGraph = clusteringGraphBuilder.build(clusteringItems, new ClusteringParameters());

    // then
    float[] expectedAreas = getExpectedAreas();
    for (int i = 0; i < clusteringItems.length; i++) {
      GraphItem graphItem = clusteringGraph.getItems().get(i);
      Assert.assertEquals(expectedAreas[i], graphItem.getArea(), 0.01f);
    }
    float[] expectedQ = getExpectedQ();
    for (int i = 0; i < clusteringItems.length; i++) {
      GraphItem graphItem = clusteringGraph.getItems().get(i);
      Assert.assertEquals(expectedQ[i], graphItem.getQ(), 0.01f);
    }
  }

  @Test
  public void shouldComputeTrianglesAreasDirectlyWithVertices() {
    // given
    ClusteringItem[] clusteringItems = getClusteringItemsWithVertices();
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();

    // do
    ClusteringGraph clusteringGraph = clusteringGraphBuilder.build(clusteringItems, new ClusteringParameters());

    // then
    float[] expectedAreas = getExpectedAreas();
    for (int i = 0; i < clusteringItems.length; i++) {
      GraphItem graphItem = clusteringGraph.getItems().get(i);
      Assert.assertEquals(expectedAreas[i], graphItem.getArea(), 0.01f);
    }
    float[] expectedQ = getExpectedQ();
    for (int i = 0; i < clusteringItems.length; i++) {
      GraphItem graphItem = clusteringGraph.getItems().get(i);
      Assert.assertEquals(expectedQ[i], graphItem.getQ(), 0.01f);
    }
  }

  private float[] getExpectedAreas() {
    return new float[] { 29318.832f, 29318.832f, 29318.832f, 104443.06f };
  }

  private float[] getExpectedQ() {
    return new float[] { 0.7473311f, 0.7473311f, 0.7473311f, 0.6677748f };
  }

  private ClusteringItem[] getClusteringItemsWithVertices() {
    Item[] items = getClusteringItems();
    for (int i = 0; i < items.length; i++) {
      items[i].setTriangleVertices(new TriangleVertices(items[i], items));
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
    items[0].setNearestItems(new NearestItem[] { new NearestItem(0, 0), new NearestItem(1, 200),  new NearestItem(2, 400), new NearestItem(3, 900) });
    items[1].setNearestItems(new NearestItem[] { new NearestItem(1, 0), new NearestItem(0, 200),  new NearestItem(2, 300), new NearestItem(3, 800) });
    items[2].setNearestItems(new NearestItem[] { new NearestItem(2, 0), new NearestItem(1, 300),  new NearestItem(0, 400), new NearestItem(3, 700) });
    items[3].setNearestItems(new NearestItem[] { new NearestItem(3, 0), new NearestItem(2, 700),  new NearestItem(1, 800), new NearestItem(0, 900) });
  }

  private Item[] getItems() {
    Item[] items = new Item[4];
    for (int i = 0; i < items.length; i++) {
      items[i] = new Item(String.valueOf(i));
    }
    return items;
  }
}
