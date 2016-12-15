package com.orange.documentare.core.comp.clustering.graph.trianglesingleton;
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
import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TriangleScissorTest {
  private final ClusteringGraph clusteringGraph = new ClusteringGraph();
  private final ClusteringParameters parameters = ClusteringParameters.builder().acut(0.5f).qcut(0.8f).build();

  private TriangleScissor triangleScissor;

  @Before
  public void setup() {
    Items items = new Items();
    clusteringGraph.getItems().add(items.get(0));
    clusteringGraph.getItems().add(items.get(1));
    clusteringGraph.getItems().add(items.get(2));
    triangleScissor = new TriangleScissor(clusteringGraph, parameters);
  }

  @Test
  public void shouldCut() {
    // given
    List<GraphItem> items = clusteringGraph.getItems();
    // do
    triangleScissor.cut();
    // then
    Assert.assertFalse(items.get(0).isTriangleSingleton());
    Assert.assertTrue(items.get(1).isTriangleSingleton());
    Assert.assertTrue(items.get(2).isTriangleSingleton());
  }
}
