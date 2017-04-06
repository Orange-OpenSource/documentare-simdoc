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
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
  public void compute_triangles_areas_with_nearest_arrays_without_knn_criteria() {
    // given
    ClusteringItem[] clusteringItems = getClusteringItems();
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = ClusteringParameters.builder().acut().qcut().build();

    // do
    ClusteringGraph clusteringGraph = clusteringGraphBuilder.buildGraphAndUpdateClusterIdAndCenter(clusteringItems, parameters);

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
  public void compute_triangles_areas_directly_with_triangle_vertices_without_knn_criteria() {
    // given
    ClusteringItem[] clusteringItems = getClusteringItemsWithVertices(-1);
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = ClusteringParameters.builder().acut().qcut().build();

    // do
    ClusteringGraph clusteringGraph = clusteringGraphBuilder.buildGraphAndUpdateClusterIdAndCenter(clusteringItems, parameters);

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
  public void compute_triangles_areas_with_nearest_arrays_with_knn_criteria() {
    // given
    ClusteringItem[] clusteringItems = getClusteringItems();
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = ClusteringParameters.builder()
            .acut()
            .qcut()
            .knn(1)
            .build();
    // do
    ClusteringGraph clusteringGraph = clusteringGraphBuilder.buildGraphAndUpdateClusterIdAndCenter(clusteringItems, parameters);

    // then
    List<GraphItem> graphItems = clusteringGraph.getItems();

    Assertions.assertThat(graphItems.get(0).iskNNSingleton()).isTrue();

    float[] expectedAreas = getExpectedAreas();
    float[] expectedQ = getExpectedQ();
    for (int i = 1; i < clusteringItems.length; i++) {
      GraphItem graphItem = graphItems.get(i);
      Assertions.assertThat(graphItem.iskNNSingleton()).isFalse();
      Assert.assertEquals(expectedAreas[i], graphItem.getArea(), 0.01f);
      Assert.assertEquals(expectedQ[i], graphItem.getQ(), 0.01f);
    }
  }

  @Test
  public void compute_triangles_areas_directly_with_triangle_vertices_with_knn_criteria() {
    // given
    ClusteringItem[] clusteringItems = getClusteringItemsWithVertices(1);
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = ClusteringParameters.builder().acut().qcut().build();
    // do
    ClusteringGraph clusteringGraph = clusteringGraphBuilder.buildGraphAndUpdateClusterIdAndCenter(clusteringItems, parameters);

    // then
    List<GraphItem> graphItems = clusteringGraph.getItems();

    Assertions.assertThat(graphItems.get(0).iskNNSingleton()).isTrue();

    float[] expectedAreas = getExpectedAreas();
    float[] expectedQ = getExpectedQ();
    for (int i = 1; i < clusteringItems.length; i++) {
      GraphItem graphItem = graphItems.get(i);
      Assertions.assertThat(graphItem.iskNNSingleton()).isFalse();
      Assert.assertEquals(expectedAreas[i], graphItem.getArea(), 0.01f);
      Assert.assertEquals(expectedQ[i], graphItem.getQ(), 0.01f);
    }
  }

  private float[] getExpectedAreas() {
    return new float[] { 58.225315f, 58.225315f, 110.244896f, 58.225315f };
  }

  private float[] getExpectedQ() {
    return new float[] { 0.30491066f, 0.30491066f, 0.26493204f, 0.30491066f };
  }

  private ClusteringItem[] getClusteringItemsWithVertices(int kNearestNeighboursThreshold) {
    Item[] items = getClusteringItems();
    int knn = kNearestNeighboursThreshold < 0 ? items.length : kNearestNeighboursThreshold;
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
