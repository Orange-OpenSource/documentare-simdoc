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
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/** Based on a given graph, check we can retrieve singletons from the built graph */
public class ClusteringGraphBuilderRetrieveSingletonsTest implements Item.ItemInit {

  @Test
  @Ignore
  public void compute_graph_and_retrieve_singletons() {
    // given
    ClusteringItem[] clusteringItems = Item.buildClusteringItems(this, 5);
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = ClusteringParameters.builder().acut(1f).qcut().build();

    // do
    ClusteringGraph clusteringGraph =
      clusteringGraphBuilder.buildGraphAndUpdateClusterIdAndCenter(clusteringItems, parameters);
    ClusteringItem[] singletons = clusteringGraphBuilder.retrieveSingletonsItemsFrom(clusteringItems, clusteringGraph);

    // then
    Assertions.assertThat(singletons).hasSize(2);
    Assertions.assertThat(singletons[0]).isEqualTo(clusteringItems[3]);
    Assertions.assertThat(singletons[1]).isEqualTo(clusteringItems[4]);
  }

  @Override
  public void init(Item[] items) {
    items[0].setNearestItems(new NearestItem[]{ new NearestItem(0, 0), new NearestItem(1, 10), new NearestItem(2, 10), new NearestItem(3, 200), new NearestItem(4, 200)});
    items[1].setNearestItems(new NearestItem[]{ new NearestItem(1, 0), new NearestItem(0, 10), new NearestItem(2, 10), new NearestItem(3, 200), new NearestItem(4, 200)});
    items[2].setNearestItems(new NearestItem[]{ new NearestItem(2, 0), new NearestItem(0, 10), new NearestItem(1, 10), new NearestItem(3, 200), new NearestItem(4, 200)});
    items[3].setNearestItems(new NearestItem[]{ new NearestItem(3, 0), new NearestItem(1, 200), new NearestItem(0, 200), new NearestItem(2, 200), new NearestItem(4, 200)});
    items[4].setNearestItems(new NearestItem[]{ new NearestItem(4, 0), new NearestItem(1, 200), new NearestItem(0, 200), new NearestItem(2, 200), new NearestItem(3, 200)});
  }
}
