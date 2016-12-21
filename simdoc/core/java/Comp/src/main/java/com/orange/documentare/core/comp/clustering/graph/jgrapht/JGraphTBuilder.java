package com.orange.documentare.core.comp.clustering.graph.jgrapht;
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
import com.orange.documentare.core.model.ref.clustering.graph.GraphEdge;
import com.orange.documentare.core.model.ref.clustering.graph.SubGraph;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class JGraphTBuilder extends GraphBuilder {
  private final boolean notUsingEdgeFiltering;

  public JGraphTBuilder() {
    this.notUsingEdgeFiltering = false;
  }

  @Override
  protected void addEdges(ClusteringGraph clusteringGraph) {
    for (SubGraph subgraph : clusteringGraph.getSubGraphs().values()) {
      addEdges(subgraph.getEdges());
    }
  }

  private void addEdges(List<GraphEdge> edges) {
    for (GraphEdge edge : edges) {
      addEdge(edge);
    }
  }

  private void addEdge(GraphEdge edge) {
    int indexVertex1 = edge.getVertex1Index();
    int indexVertex2 = edge.getVertex2Index();
    int len = edge.getLength();
    JGraphEdge e = getGraph().addEdge(getGraphItems().get(indexVertex1), getGraphItems().get(indexVertex2));
    e.init(indexVertex1, indexVertex2, len);
    getGraph().setEdgeWeight(e, len);
  }
}