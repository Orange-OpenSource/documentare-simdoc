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
import com.orange.documentare.core.comp.clustering.graph.trianglesingleton.TriangleScissor;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
class TriangulationTreatments {
  private final List<GraphItem> graphItems;
  private final ClusteringParameters clusteringParameters;

  private int stabilityLoopCount = -1;
  private int singletons;

  void doTreatments() {
    incrementalSingletonDetection();
    log.info("Initial triangulation build (q/area), {} loop(s), singleton(s) = {}", stabilityLoopCount, singletons);
  }

  private void incrementalSingletonDetection() {
    TriangleScissor triangleScissor = new TriangleScissor(graphItems, clusteringParameters);
    int cutSingletons;
    do {
      cutSingletons = triangleScissor.cut();
      singletons += cutSingletons;
      stabilityLoopCount++;
      log.debug("triangulation scissor loop, found {} singleton(s)", cutSingletons);
    } while (cutSingletons != 0);
  }

}
