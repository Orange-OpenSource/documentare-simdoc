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

import com.orange.documentare.core.comp.clustering.graph.jgrapht.JGraphEdge;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.SubgraphsBuilder;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.TriangulationGraphBuilder;
import com.orange.documentare.core.comp.clustering.graph.scissors.ClusteringParameters;
import com.orange.documentare.core.comp.clustering.graph.trianglesingleton.TriangleScissor;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jgrapht.Graph;

@Log4j2
@RequiredArgsConstructor
class Triangulation {
  private final ClusteringGraph clusteringGraph;
  private final ClusteringParameters clusteringParameters;

  private int stabilityLoopCount = -1;
  private int singletons;

  Graph<GraphItem, JGraphEdge> getTriangulationGraph() {
    incrementalSingletonDetection();
    Graph<GraphItem, JGraphEdge> graph = computeClusteringGraph();
    log.info("Initial triangulation build (q/area), {} loop(s), subgraph(s) = {}, singleton(s) = {}", stabilityLoopCount, clusteringGraph.getSubGraphs().size(), singletons);
    return graph;
  }

  private void incrementalSingletonDetection() {
    TriangleScissor triangleScissor = new TriangleScissor(clusteringGraph, clusteringParameters);
    int cutSingletons;
    do {
      cutSingletons = triangleScissor.cut();
      singletons += cutSingletons;
      stabilityLoopCount++;
      log.debug("triangulation scissor loop, found {} singleton(s)", cutSingletons);
    } while (cutSingletons != 0);
  }

  private Graph computeClusteringGraph() {
    TriangulationGraphBuilder triangulationGraphBuilder = new TriangulationGraphBuilder();
    SubgraphsBuilder subgraphsBuilder = new SubgraphsBuilder(clusteringGraph, triangulationGraphBuilder);
    return subgraphsBuilder.computeSubGraphs();
  }
}
