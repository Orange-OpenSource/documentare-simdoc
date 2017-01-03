package com.orange.documentare.simdoc.server.biz.clustering;

/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ClusteringOutputCoherenceTest {

  @Test
  public void incoherence_throws_illegal_state_exception() {
    // Given

    // When
    IllegalStateException actualException = null;
    try {
      buildIncoherentClusteringOutput();
    } catch (IllegalStateException e) {
      actualException = e;
    }

    // Then
    Assertions.assertThat(actualException.getMessage()).contains("cluster id not coherent: 12 vs 34");
  }

  private ClusteringOutput buildIncoherentClusteringOutput() {
    SimClusteringItem simClusteringItem = new SimClusteringItem("/");
    simClusteringItem.setClusterId(34);
    SimClusteringItem[] simClusteringItems = { simClusteringItem };

    GraphItem graphItem = new GraphItem();
    graphItem.setClusterId(12);
    ClusteringGraph graph = new ClusteringGraph();
    graph.getItems().add(graphItem);

    return new ClusteringOutput(simClusteringItems, graph);
  }
}
