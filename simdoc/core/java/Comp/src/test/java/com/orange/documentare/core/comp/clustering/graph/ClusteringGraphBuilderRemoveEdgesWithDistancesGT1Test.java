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

import com.orange.documentare.core.comp.distance.Distance;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphEdge;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.comp.TriangleVertices;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.List;

public class ClusteringGraphBuilderRemoveEdgesWithDistancesGT1Test {
  private static final int D = Distance.DISTANCE_INT_CONV_FACTOR;

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
  public void edges_have_normal_distances_so_keep_all_edges() {
    // given
    Item[] clusteringItems = initItems();
    fillWithNormalDistances(clusteringItems);
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = ClusteringParameters.builder().build();

    // do
    ClusteringGraph clusteringGraph = clusteringGraphBuilder.build(clusteringItems, parameters);

    // then
    List<GraphEdge> edges = clusteringGraph.getSubGraphs().get(0).getEdges();
    Assertions.assertThat(edges).hasSize(9);
    edges.stream()
      .map(edge -> edge.getLength())
      .forEach(length ->
      Assertions.assertThat(length).isLessThan(Distance.DISTANCE_INT_CONV_FACTOR)
    );
  }

  @Test
  public void edges_longer_or_equal_to_1_are_removed_as_third_edge() {
    // given
    Item[] clusteringItems = initItems();
    fillWithAnInvalidThirdEdgeDistance(clusteringItems);
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = ClusteringParameters.builder().build();

    // do
    ClusteringGraph clusteringGraph = clusteringGraphBuilder.build(clusteringItems, parameters);

    // then
    List<GraphEdge> edges = clusteringGraph.getSubGraphs().get(0).getEdges();
    Assertions.assertThat(edges).hasSize(6);
    edges.stream()
      .map(edge -> edge.getLength())
      .forEach(length ->
        Assertions.assertThat(length).isLessThan(Distance.DISTANCE_INT_CONV_FACTOR)
      );
  }

  @Test
  public void edges_longer_or_equal_to_1_are_removed_as_second_or_third_edge() {
    // given
    Item[] clusteringItems = initItems();
    fillWithAnInvalidSecondOrThirdEdgeDistance(clusteringItems);
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = ClusteringParameters.builder().build();

    // do
    ClusteringGraph clusteringGraph = clusteringGraphBuilder.build(clusteringItems, parameters);

    // then
    List<GraphEdge> edges = clusteringGraph.getSubGraphs().get(0).getEdges();
    Assertions.assertThat(edges).hasSize(4);
    edges.stream()
      .map(edge -> edge.getLength())
      .forEach(length ->
        Assertions.assertThat(length).isLessThan(Distance.DISTANCE_INT_CONV_FACTOR)
      );
  }

  private void fillWithAnInvalidSecondOrThirdEdgeDistance(Item[] items) {
    items[0].setNearestItems(new NearestItem[]{ new NearestItem(0, 0), new NearestItem(1, D - 10), new NearestItem(2, D - 10)});
    items[1].setNearestItems(new NearestItem[]{ new NearestItem(1, 0), new NearestItem(0, D), new NearestItem(2, D)});
    items[2].setNearestItems(new NearestItem[]{ new NearestItem(2, 0), new NearestItem(0, D), new NearestItem(1, D)});
  }

  private void fillWithAnInvalidThirdEdgeDistance(Item[] items) {
    items[0].setNearestItems(new NearestItem[]{ new NearestItem(0, 0), new NearestItem(1, D - 10), new NearestItem(2, D - 10)});
    items[1].setNearestItems(new NearestItem[]{ new NearestItem(1, 0), new NearestItem(0, D - 10), new NearestItem(2, D)});
    items[2].setNearestItems(new NearestItem[]{ new NearestItem(2, 0), new NearestItem(0, D - 10), new NearestItem(1, D)});
  }

  private void fillWithNormalDistances(Item[] items) {
    items[0].setNearestItems(new NearestItem[]{ new NearestItem(0, 0), new NearestItem(1, D - 10), new NearestItem(2, D - 10)});
    items[1].setNearestItems(new NearestItem[]{ new NearestItem(1, 0), new NearestItem(0, D - 10), new NearestItem(2, D - 10)});
    items[2].setNearestItems(new NearestItem[]{ new NearestItem(2, 0), new NearestItem(0, D - 10), new NearestItem(1, D - 10)});
  }

  private Item[] initItems() {
    Item[] items = new Item[3];
    for (int i = 0; i < items.length; i++) {
      items[i] = new Item(String.valueOf(i));
    }
    return items;
  }
}
