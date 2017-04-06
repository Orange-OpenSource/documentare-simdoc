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

import com.orange.documentare.core.comp.clustering.graph.InputItem;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ClusterTreatmentsTest {

  private static final int NB_ITEMS = 5;
  private static final int CENTER_1_INDEX = 1;
  private static final int CENTER_2_INDEX = 4;

  @Test
  public void updateClusteringItemsCenters() {
    // given
    ClusteringGraph clusteringGraph = buildClusteringGraph();
    ClusterTreatments clusterTreatments = new ClusterTreatments(clusteringGraph, null, buildClusteringItems());
    // do
    clusterTreatments.updateClusteringItemsCenters();
    // then
    IntStream.range(0, NB_ITEMS).forEach(i ->
              Assertions.assertThat(clusteringGraph.getItems().get(i).isClusterCenter())
                      .isEqualTo(i == CENTER_1_INDEX || i== CENTER_2_INDEX)
    );
  }

  private ClusteringGraph buildClusteringGraph() {
    List<GraphItem> graphItems = new ArrayList<>();
    IntStream.range(0, NB_ITEMS).forEach(i -> graphItems.add(new GraphItem()));
    IntStream.range(0, NB_ITEMS).forEach(i -> graphItems.get(i).setVertex1Index(i));
    graphItems.get(CENTER_1_INDEX).setClusterCenter(true);
    graphItems.get(CENTER_2_INDEX).setClusterCenter(true);
    ClusteringGraph clusteringGraph = new ClusteringGraph(graphItems);
    return clusteringGraph;
  }

  private ClusteringItem[] buildClusteringItems() {
    ClusteringItem[] clusteringItems = new ClusteringItem[NB_ITEMS];
    IntStream.range(0, NB_ITEMS).forEach(i -> clusteringItems[i] = new InputItem());
    clusteringItems[CENTER_1_INDEX].setClusterCenter(true);
    clusteringItems[CENTER_2_INDEX].setClusterCenter(true);
    return clusteringItems;
  }
}
