package com.orange.documentare.core.comp.clustering.graph.scissors.longedges;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.Items;
import com.orange.documentare.core.model.ref.clustering.graph.GraphEdge;
import com.orange.documentare.core.model.ref.clustering.graph.GraphGroup;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LongEdgesTriggerTest {
  private static final float STD_FACTOR = 2;
  private final List<GraphItem> graphItems = getGraphItems();

  private LongEdgesTrigger longEdgesTrigger;

  @Before
  public void setup() {
    longEdgesTrigger = new LongEdgesTrigger(STD_FACTOR);
  }

  @Test
  public void shouldComputeThreshold() {
    // given
    GraphGroup graphGroup = getGraphGroup();
    // do
    longEdgesTrigger.initForGroup(graphGroup);
    // then
    Assert.assertEquals(53, longEdgesTrigger.getEdgeLengthThreshold(), 0);
  }

  @Test
  public void shouldCutOrNot() {
    // given
    GraphGroup graphGroup = getGraphGroup();
    longEdgesTrigger.initForGroup(graphGroup);
    // do
    // then
    for (int i = 0; i < 5; i++) {
      Assert.assertFalse(longEdgesTrigger.shouldRemove(graphGroup.getEdges().get(i)));
    }
    Assert.assertTrue(longEdgesTrigger.shouldRemove(graphGroup.getEdges().get(5)));
  }

  private List<GraphItem> getGraphItems() {
    return new Items();
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
    for (int i = 0; i < 4; i++) {
      graphGroup.getEdges().add(new GraphEdge(0, 1, 10));
    }
    graphGroup.getEdges().add(new GraphEdge(1, 2, 50));
    graphGroup.getEdges().add(new GraphEdge(2, 1, 300));
    return graphGroup;
  }
}
