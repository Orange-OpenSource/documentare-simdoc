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
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TriangleScissorTriggerTest {
  private final ClusteringParameters parameters = ClusteringParameters.builder().acut(0.5f).qcut(0.8f).build();
  private final List<GraphItem> items = new Items();

  private TriangleScissorTrigger scissorTrigger;

  @Before
  public void setup() {
    scissorTrigger = new TriangleScissorTrigger(items, parameters);
  }

  @Test
  public void shouldBuildStats() {
    // then
    Assert.assertEquals(0.602, scissorTrigger.getQThreshold(), 0.001);
    Assert.assertEquals(6598, scissorTrigger.getAreaThreshold(), 1);
  }

  @Test
  public void shouldCutOrNot() {
    // then
    Assert.assertFalse(scissorTrigger.shouldCut(items.get(0)));
    Assert.assertTrue(scissorTrigger.shouldCut(items.get(1)));
    Assert.assertTrue(scissorTrigger.shouldCut(items.get(2)));
  }
}
