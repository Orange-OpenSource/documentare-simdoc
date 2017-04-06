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

import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.core.comp.clustering.graph.Items;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TriangleScissorTest {
  private ClusteringGraph clusteringGraph;

  @Before
  public void setup() {
    Items items = new Items();
    clusteringGraph = new ClusteringGraph(items);
  }

  @Test
  public void acut_and_qcut() {
    // given
    ClusteringParameters parameters = ClusteringParameters.builder().acut(0.5f).qcut(0.8f).build();
    List<GraphItem> items = clusteringGraph.getItems();
    TriangleScissor triangleScissor = new TriangleScissor(items, parameters);

    // do
    triangleScissor.cut();
    // then
    Assert.assertFalse(items.get(0).isTriangleSingleton());
    Assert.assertTrue(items.get(1).isTriangleSingleton());
    Assert.assertTrue(items.get(2).isTriangleSingleton());
  }

  @Test
  public void acut() {
    // given
    ClusteringParameters parameters = ClusteringParameters.builder().acut(0.5f).build();
    List<GraphItem> items = clusteringGraph.getItems();
    TriangleScissor triangleScissor = new TriangleScissor(items, parameters);

    // do
    triangleScissor.cut();
    // then
    Assert.assertFalse(items.get(0).isTriangleSingleton());
    Assert.assertTrue(items.get(1).isTriangleSingleton());
    Assert.assertFalse(items.get(2).isTriangleSingleton());
  }

  @Test
  public void qcut() {
    // given
    ClusteringParameters parameters = ClusteringParameters.builder().qcut(0.8f).build();
    List<GraphItem> items = clusteringGraph.getItems();
    TriangleScissor triangleScissor = new TriangleScissor(items, parameters);

    // do
    triangleScissor.cut();
    // then
    Assert.assertFalse(items.get(0).isTriangleSingleton());
    Assert.assertFalse(items.get(1).isTriangleSingleton());
    Assert.assertTrue(items.get(2).isTriangleSingleton());
  }

  @Test
  public void no_cut() {
    // given
    ClusteringParameters parameters = ClusteringParameters.builder().build();
    List<GraphItem> items = clusteringGraph.getItems();
    TriangleScissor triangleScissor = new TriangleScissor(items, parameters);

    // do
    triangleScissor.cut();
    // then
    items.stream().forEach(item -> Assertions.assertThat(item.isTriangleSingleton()).isFalse());
  }
}
