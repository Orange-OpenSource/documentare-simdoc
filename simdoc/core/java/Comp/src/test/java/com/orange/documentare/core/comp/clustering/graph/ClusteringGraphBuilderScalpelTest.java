package com.orange.documentare.core.comp.clustering.graph;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphCluster;
import com.orange.documentare.core.model.ref.clustering.graph.SubGraph;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import org.fest.assertions.Assertions;
import org.junit.runner.RunWith;

import java.util.Map;

@RunWith(ZohhakRunner.class)
public class ClusteringGraphBuilderScalpelTest implements Item.ItemInit {

  @TestWith({"false, 3, 1, 2", "false, 1.96, 3, 3", "true, 3, 3, 3"})
  public void compute_graph_with_scut(boolean sloop, float scut, int subGraphsNb, int clustersNb) {
    // given
    ClusteringItem[] clusteringItems = Item.buildClusteringItems(this, 6);
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = sloop ?
      ClusteringParameters.builder().scut(scut).sloop().build() :
      ClusteringParameters.builder().scut(scut).build();

    // do
    ClusteringGraph clusteringGraph =
      clusteringGraphBuilder.buildGraphAndUpdateClusterIdAndCenter(clusteringItems, parameters);

    // then
    Map<Integer, SubGraph> subGraphs = clusteringGraph.getSubGraphs();
    Map<Integer, GraphCluster> clusters = clusteringGraph.getClusters();
    Assertions.assertThat(subGraphs).hasSize(subGraphsNb);
    Assertions.assertThat(clusters).hasSize(clustersNb);
  }

  @TestWith({"false, 1.96", "true, 3"})
  public void check_items_are_in_correct_cluster(boolean sloop, float scut) {
    // given
    ClusteringItem[] clusteringItems = Item.buildClusteringItems(this, 6);
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = sloop ?
      ClusteringParameters.builder().scut(scut).sloop().build() :
      ClusteringParameters.builder().scut(scut).build();

    // do
    clusteringGraphBuilder.buildGraphAndUpdateClusterIdAndCenter(clusteringItems, parameters);

    // then
    Assertions.assertThat(clusteringItems[0].getClusterId()).isEqualTo(1);
    Assertions.assertThat(clusteringItems[1].getClusterId()).isEqualTo(1);
    Assertions.assertThat(clusteringItems[2].getClusterId()).isEqualTo(1);
    Assertions.assertThat(clusteringItems[3].getClusterId()).isEqualTo(2);
    Assertions.assertThat(clusteringItems[4].getClusterId()).isEqualTo(3);
    Assertions.assertThat(clusteringItems[5].getClusterId()).isEqualTo(3);
  }

  @Override
  public void init(Item[] items) {
    items[0].setNearestItems(new NearestItem[]{ new NearestItem(0, 0), new NearestItem(1, 5), new NearestItem(2, 5), new NearestItem(3, 10), new NearestItem(4, 40), new NearestItem(5, 45)});
    items[1].setNearestItems(new NearestItem[]{ new NearestItem(1, 0), new NearestItem(0, 5), new NearestItem(2, 5), new NearestItem(3, 10), new NearestItem(4, 45), new NearestItem(5, 47)});
    items[2].setNearestItems(new NearestItem[]{ new NearestItem(2, 0), new NearestItem(0, 5), new NearestItem(1, 5), new NearestItem(3, 10), new NearestItem(4, 45), new NearestItem(5, 47)});
    items[3].setNearestItems(new NearestItem[]{ new NearestItem(3, 0), new NearestItem(0, 10), new NearestItem(1, 10), new NearestItem(2, 10), new NearestItem(4, 45), new NearestItem(5, 47)});

    items[4].setNearestItems(new NearestItem[]{ new NearestItem(4, 0), new NearestItem(5, 5), new NearestItem(0, 40), new NearestItem(1, 47), new NearestItem(2, 47), new NearestItem(3, 47)});
    items[5].setNearestItems(new NearestItem[]{ new NearestItem(5, 0), new NearestItem(4, 5), new NearestItem(0, 45), new NearestItem(1, 47), new NearestItem(2, 47), new NearestItem(3, 47)});
  }
}
