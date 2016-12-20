package com.orange.documentare.core.comp.clustering.graph.scissors;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.clustering.graph.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.*;

@Log4j2
/**
 * Generic implementation of the graph scissor concept:
 *  - can work on all graph graphItems individually
 *  - or on graphItems per group, based on group statistics
 */
public abstract class GraphScissor {

  @Getter(AccessLevel.PROTECTED)
  private final ClusteringGraph clusteringGraph;

  private final Collection<? extends GraphGroup> groups;

  /** Number of edges which were cut */
  private int edgesCutInGraph;

  /** Memorize if a group is stable, in order to avoid trying to clean it again for nothing */
  private Map<GraphGroup, Boolean> stableGroups = new HashMap<>();

  protected GraphScissor(ClusteringGraph clusteringGraph, Collection<? extends GraphGroup> groups) {
    this.clusteringGraph = clusteringGraph;
    this.groups = groups;
    initStableGroups(groups);
  }

  private void initStableGroups(Collection<? extends GraphGroup> groups) {
    for (GraphGroup group : groups) {
      boolean stable = group.getItemIndices().size() == 1;
      stableGroups.put(group, stable);
    }
  }

  /**
   * Clean groups
   * @return the number of cut edges
   */
  public int clean() {
    edgesCutInGraph = 0;
    for (GraphGroup group : groups) {
      if (!stableGroups.get(group)) {
        clean(group);
      }
    }
    return edgesCutInGraph;
  }

  private void clean(GraphGroup group) {
    getScissorTrigger().initForGroup(group);
    List<GraphEdge> edgesToRemove = new ArrayList<>();
    ScissorTrigger scissorTrigger = getScissorTrigger();
    for (GraphEdge edge : group.getEdges()) {
      if (scissorTrigger.shouldRemove(edge)) {
        edgesToRemove.add(edge);
      }
    }
    removeEdges(edgesToRemove, group);
  }

  private void removeEdges(List<GraphEdge> edgesToRemove, GraphGroup group) {
    edgesCutInGraph += edgesToRemove.size();
    group.getEdges().removeAll(edgesToRemove);
    if (group instanceof GraphCluster) {
      removeEdgesInSubgraph(edgesToRemove, group);
    }
    boolean stable = edgesCutInGraph == 0 || group.getEdges().size() == 0;
    stableGroups.put(group, stable);
    debugCutVertices(edgesToRemove, group, stable);
  }

  private void removeEdgesInSubgraph(List<GraphEdge> edgesToRemove, GraphGroup group) {
    GraphCluster graphCluster = (GraphCluster)group;
    clusteringGraph.getSubGraphs().get(graphCluster.getSubgraphId()).getEdges().removeAll(edgesToRemove);
  }

  private void debugCutVertices(List<GraphEdge> edgesToRemove, GraphGroup group, boolean stable) {
    if (log.isDebugEnabled()) {
      log.debug("Remove connections in group {} (stable = {})", group.getGroupId(), stable);
      for (GraphEdge edge : edgesToRemove) {
        log.debug("Remove connection between {} and {}, length {}", clusteringGraph.getItems().get(edge.getVertex1Index()).getVertexName(), clusteringGraph.getItems().get(edge.getVertex2Index()).getVertexName(), edge.getLength());
      }
    }
  }

  protected abstract ScissorTrigger getScissorTrigger();
}
