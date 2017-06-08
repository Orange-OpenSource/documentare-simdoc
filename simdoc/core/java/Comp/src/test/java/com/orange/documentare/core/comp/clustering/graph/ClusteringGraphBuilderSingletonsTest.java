package com.orange.documentare.core.comp.clustering.graph;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Denis Boisset & Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

/** Based on a given graph, check we can retrieve singletons from the built graph */
public class ClusteringGraphBuilderSingletonsTest implements Item.ItemInit {

  @Test
  public void compute_graph_and_retrieve_singletons() {
    // given
    ClusteringItem[] clusteringItems = Item.buildClusteringItems(this, 7);
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = ClusteringParameters.builder().acut(0.1f).qcut().build();

    // do
    ClusteringGraph clusteringGraph =
      clusteringGraphBuilder.buildGraphAndUpdateClusterIdAndCenter(clusteringItems, parameters);
    ClusteringItem[] singletons = clusteringGraphBuilder.retrieveSingletonsItemsFrom(clusteringItems, clusteringGraph);

    // then
    Assertions.assertThat(singletons).hasSize(4);
    Assertions.assertThat(singletons).contains(clusteringItems[3], clusteringItems[4], clusteringItems[5], clusteringItems[6]);
  }

  @Test
  public void build_new_singletons_objects_to_do_second_graph_build_on_singletons_only() {
    // given
    ClusteringItem[] clusteringItems = Item.buildClusteringItems(this, 7);
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = ClusteringParameters.builder().acut(0.1f).qcut().build();

    ClusteringGraph clusteringGraph =
      clusteringGraphBuilder.buildGraphAndUpdateClusterIdAndCenter(clusteringItems, parameters);
    ClusteringItem[] singletons = clusteringGraphBuilder.retrieveSingletonsItemsFrom(clusteringItems, clusteringGraph);

    // To make sure we can rely on singleton index in our assertions
    Arrays.sort(singletons, Comparator.comparing(ClusteringItem::getHumanReadableId));

    // do
    SingletonForReGraph[] singletonsForRegraph = clusteringGraphBuilder.buildSingletonsForRegraph(clusteringItems, singletons);

    // then
    Assertions.assertThat(singletons).hasSize(4);
    SingletonForReGraph singleton0 = singletonsForRegraph[0];
    Assertions.assertThat(singleton0.getNearestItems()[0].getIndex()).isEqualTo(0);
    Assertions.assertThat(singleton0.getNearestItems()[1].getIndex()).isEqualTo(1);
    Assertions.assertThat(singleton0.getNearestItems()[2].getIndex()).isEqualTo(2);
    Assertions.assertThat(singleton0.getNearestItems()[3].getIndex()).isEqualTo(3);

    SingletonForReGraph singleton2 = singletonsForRegraph[2];
    Assertions.assertThat(singleton2.getNearestItems()[0].getIndex()).isEqualTo(2);
    Assertions.assertThat(singleton2.getNearestItems()[1].getIndex()).isEqualTo(1);
    Assertions.assertThat(singleton2.getNearestItems()[2].getIndex()).isEqualTo(0);
    Assertions.assertThat(singleton2.getNearestItems()[3].getIndex()).isEqualTo(3);
  }

  @Override
  public void init(Item[] items) {
    items[0].setNearestItems(new NearestItem[]{ new NearestItem(0, 0), new NearestItem(1, 10), new NearestItem(2, 10), new NearestItem(3, 200), new NearestItem(4, 200), new NearestItem(5, 200), new NearestItem(6, 1000)});
    items[1].setNearestItems(new NearestItem[]{ new NearestItem(1, 0), new NearestItem(0, 10), new NearestItem(2, 10), new NearestItem(3, 200), new NearestItem(4, 200), new NearestItem(5, 200), new NearestItem(6, 1000)});
    items[2].setNearestItems(new NearestItem[]{ new NearestItem(2, 0), new NearestItem(0, 10), new NearestItem(1, 10), new NearestItem(3, 200), new NearestItem(4, 200), new NearestItem(5, 200), new NearestItem(6, 1000)});

    items[3].setNearestItems(new NearestItem[]{ new NearestItem(3, 0), new NearestItem(4, 100), new NearestItem(5, 102), new NearestItem(0, 200), new NearestItem(1, 200), new NearestItem(2, 200), new NearestItem(6, 1000)});
    items[4].setNearestItems(new NearestItem[]{ new NearestItem(4, 0), new NearestItem(3, 100), new NearestItem(5, 101), new NearestItem(0, 200), new NearestItem(1, 200), new NearestItem(2, 200), new NearestItem(6, 1000)});
    items[5].setNearestItems(new NearestItem[]{ new NearestItem(5, 0), new NearestItem(4, 101), new NearestItem(3, 102), new NearestItem(0, 200), new NearestItem(1, 200), new NearestItem(2, 200), new NearestItem(6, 1000)});

    items[6].setNearestItems(new NearestItem[]{ new NearestItem(6, 0), new NearestItem(0, 1000), new NearestItem(1, 1000), new NearestItem(2, 1000), new NearestItem(3, 1000), new NearestItem(4, 1000), new NearestItem(5, 1000)});
  }
}
