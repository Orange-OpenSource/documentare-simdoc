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

import com.orange.documentare.core.comp.clustering.graph.subgraphs.SameSubgraph;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import com.orange.documentare.core.model.ref.clustering.graph.SubGraph;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.jgrapht.Graph;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class SubgraphsBuilder {
  private final ClusteringGraph clusteringGraph;
  private final GraphBuilder graphBuilder;

  public Graph computeSubGraphs() {
    Graph graph = detectSubGraphs();
    setSubgraphsEdges(graph);
    return graph;
  }

  private Graph detectSubGraphs() {
    Graph graph = graphBuilder.getJGraphTFrom(clusteringGraph);
    List<Collection<Integer>> jSubGraphs = graphBuilder.getAndUpdateSubGraphs(graph);
    setSubGraphsFrom(jSubGraphs);
    return graph;
  }

  private void setSubGraphsFrom(List<Collection<Integer>> jSubGraphs) {
    Map<Integer, SubGraph> subGraphs = clusteringGraph.getSubGraphs();
    subGraphs.clear();
    for (int i = 0; i < jSubGraphs.size(); i++) {
      subGraphs.put(i, getSubGraphFrom(jSubGraphs.get(i), i));
    }
  }

  private SubGraph getSubGraphFrom(Collection<Integer> jSubGraph, int subGraphId) {
    SummaryStatistics summaryStatisticsQ = new SummaryStatistics();
    SummaryStatistics summaryStatisticsArea = new SummaryStatistics();
    List<GraphItem> graphItems = clusteringGraph.getItems();
    for (Integer i : jSubGraph) {
      GraphItem graphItem = graphItems.get(i);
      graphItem.setSubgraphId(subGraphId);
      summaryStatisticsQ.addValue(graphItem.getQ());
      summaryStatisticsArea.addValue(graphItem.getArea());
    }
    return new SubGraph(subGraphId, jSubGraph, summaryStatisticsQ, summaryStatisticsArea);
  }

  private void setSubgraphsEdges(Graph graph) {
    SameGroup sameGroup = new SameSubgraph();
    GroupEdges groupEdges = new GroupEdges(clusteringGraph.getItems(), clusteringGraph.getSubGraphs(), sameGroup);
    groupEdges.updateGroupsEdges(graph.edgeSet());
  }
}
