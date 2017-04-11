package com.orange.documentare.core.comp.clustering.graph.scissors.clusterlongedges;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.GraphItems;
import com.orange.documentare.core.model.ref.clustering.graph.GraphEdge;
import com.orange.documentare.core.model.ref.clustering.graph.GraphGroup;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ClusterLongEdgesTriggerTest {
  private static final float PERCENTILE = 75;
  private final List<GraphItem> graphItems = getGraphItems();

  @Getter(AccessLevel.PACKAGE)
  private ClusterLongEdgesTrigger clusterLongEdgesTrigger;

  @Before
  public void setup() {
    clusterLongEdgesTrigger = new ClusterLongEdgesTrigger(graphItems, PERCENTILE);
  }

  @Test
  public void shouldComputeThreshold() {
    // given
    GraphGroup graphGroup = getGraphGroup();
    // do
    clusterLongEdgesTrigger.initForGroup(graphGroup);
    // then
    Assert.assertEquals(250, clusterLongEdgesTrigger.getEdgeLengthThreshold(), 0);
  }

  @Test
  public void shouldCutOrNot() {
    // given
    GraphGroup graphGroup = getGraphGroup();
    clusterLongEdgesTrigger.initForGroup(graphGroup);
    // do
    // then
    for (int i = 0; i < 3; i++) {
      Assert.assertFalse(clusterLongEdgesTrigger.shouldRemove(graphGroup.getEdges().get(i)));
    }
    Assert.assertTrue(clusterLongEdgesTrigger.shouldRemove(graphGroup.getEdges().get(3)));
  }

  private List<GraphItem> getGraphItems() {
    return new GraphItems();
  }

  private GraphGroup getGraphGroup() {
    GraphGroup graphGroup = new GraphGroup() {
      @Override
      public List<Integer> getItemIndices() {
        List<Integer> itemIndices = new ArrayList<>();
        for (int i = 0; i < graphItems.size(); i++) {
          itemIndices.add(i);
        }
        return itemIndices;
      }
    };
    graphGroup.getEdges().add(new GraphEdge(0, 1, 50));
    graphGroup.getEdges().add(new GraphEdge(1, 0, 50));
    graphGroup.getEdges().add(new GraphEdge(0, 2, 100));
    graphGroup.getEdges().add(new GraphEdge(1, 2, 300));
    return graphGroup;
  }
}
