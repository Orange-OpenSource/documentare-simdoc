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

public class ClusteringGraphBuilderScalpelTest implements Item.ItemInit {

  @Test
  public void compute_triangles_areas() {
    // given
    ClusteringItem[] clusteringItems = Item.buildClusteringItems(this, 7);
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = ClusteringParameters.builder().scut(2f).build();
    // do
    ClusteringGraph clusteringGraph =
      clusteringGraphBuilder.buildGraphAndUpdateClusterIdAndCenter(clusteringItems, parameters);

    // then
    List<GraphItem> graphItems = clusteringGraph.getItems();

    // TODO
  }

  @Override
  public void init(Item[] items) {
    items[0].setNearestItems(new NearestItem[]{ new NearestItem(0, 0), new NearestItem(1, 5), new NearestItem(2, 5), new NearestItem(3, 10), new NearestItem(4, 40), new NearestItem(5, 45), new NearestItem(6, 45)});
    items[1].setNearestItems(new NearestItem[]{ new NearestItem(1, 0), new NearestItem(0, 5), new NearestItem(2, 5), new NearestItem(3, 10), new NearestItem(4, 45), new NearestItem(5, 47), new NearestItem(6, 45)});
    items[2].setNearestItems(new NearestItem[]{ new NearestItem(2, 0), new NearestItem(0, 5), new NearestItem(1, 5), new NearestItem(3, 10), new NearestItem(4, 45), new NearestItem(5, 47), new NearestItem(6, 45)});
    items[3].setNearestItems(new NearestItem[]{ new NearestItem(3, 0), new NearestItem(0, 10), new NearestItem(1, 10), new NearestItem(2, 10), new NearestItem(4, 45), new NearestItem(5, 47), new NearestItem(6, 45)});

    items[4].setNearestItems(new NearestItem[]{ new NearestItem(4, 0), new NearestItem(5, 5), new NearestItem(6, 5), new NearestItem(0, 40), new NearestItem(1, 47), new NearestItem(2, 47), new NearestItem(3, 47)});
    items[5].setNearestItems(new NearestItem[]{ new NearestItem(5, 0), new NearestItem(4, 5), new NearestItem(6, 5), new NearestItem(0, 45), new NearestItem(1, 47), new NearestItem(2, 47), new NearestItem(3, 47)});
    items[6].setNearestItems(new NearestItem[]{ new NearestItem(6, 0), new NearestItem(4, 5), new NearestItem(5, 5), new NearestItem(0, 45), new NearestItem(1, 47), new NearestItem(2, 47), new NearestItem(3, 47)});
  }
}
