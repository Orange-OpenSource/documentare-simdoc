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
import com.orange.documentare.core.comp.clustering.graph.scissors.ClusteringParameters;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TriangleScissorTest {
  private final ClusteringParameters clusteringParameters = new ClusteringParameters();
  private final ClusteringGraph clusteringGraph = new ClusteringGraph();
  private TriangleScissor triangleScissor;

  @Before
  public void setup() {
    clusteringParameters.setStdQFactor(0.8f);
    clusteringParameters.setStdAreaFactor(0.5f);
    Items items = new Items();
    clusteringGraph.getItems().add(items.get(0));
    clusteringGraph.getItems().add(items.get(1));
    clusteringGraph.getItems().add(items.get(2));
    triangleScissor = new TriangleScissor(clusteringGraph, clusteringParameters);
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
