package com.orange.documentare.core.comp.clustering.graph.subgraphs;
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
import com.orange.documentare.core.comp.clustering.graph.scissors.longedges.LongEdgesScissor;
import com.orange.documentare.core.comp.clustering.graph.scissors.nonminimaledges.NonMinimalEdgesScissor;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.SubGraph;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor(suppressConstructorProperties = true)
@Slf4j
public class SubGraphTreatments {
  private final ClusteringGraph clusteringGraph;
  private final ClusteringParameters clusteringParameters;

  private int stabilityLoopCount;
  private int edgesCutTotalCount;

  public void doTreatments(float scutSdFactor) {
    cutInvalidDistances();
    if (clusteringParameters.wcut) {
      cutNonMinimalVertices();
    } else if (clusteringParameters.scut()) {
      cutLongestVertices(scutSdFactor);
    }
  }

  private void cutInvalidDistances() {
    InvalidEdgeDistanceScissor invalidEdgeDistanceScissor = new InvalidEdgeDistanceScissor(clusteringGraph, clusteringGraph.getSubGraphs().values());
    int edgesCut = invalidEdgeDistanceScissor.cut();
    if (edgesCut > 0) {
      log.info("Invalid distances (>=1) cut, edges cut = {}", edgesCut);
    }
  }

  private void cutNonMinimalVertices() {
    NonMinimalEdgesScissor minimalEdgesScissor = new NonMinimalEdgesScissor(clusteringGraph, clusteringGraph.getSubGraphs().values());
    minimalEdgesScissor.cut();
    log.info("Wonder cut");
  }

  private void cutLongestVertices(float scutSdFactor) {
    Collection<SubGraph> subgraphs = buildEligibleSubgraphsList();
    LongEdgesScissor longEdgesScissor = new LongEdgesScissor(clusteringGraph, subgraphs, scutSdFactor);
    stabilityLoopCount = 0;
    edgesCutTotalCount = 0;
    while(loopOnRemoveStaticallyLongestVertices(longEdgesScissor));
    if (!clusteringParameters.sloop) {
      log.info("Scalpel cut, loops = {}, edges cut = {}", stabilityLoopCount, edgesCutTotalCount);
    }
  }

  private Collection<SubGraph> buildEligibleSubgraphsList() {
    return clusteringGraph.getSubGraphs().values().stream()
      .filter(subGraph -> {
        // For "Scut loop" mode, keep on working on subgraph which contains no (first round) or more than one cluster
        return !(subGraph.getClusterIndices().size() == 1);
      })
      .collect(Collectors.toList());
  }

  private boolean loopOnRemoveStaticallyLongestVertices(LongEdgesScissor longEdgesScissor) {
    int edgesCut = longEdgesScissor.cut();
    stabilityLoopCount++;
    edgesCutTotalCount += edgesCut;
    log.debug("Subgraph scut iterations = {}, edges cut = {}", stabilityLoopCount, edgesCut);
    return edgesCut != 0;
  }
}
