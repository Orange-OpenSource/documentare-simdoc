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
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import lombok.AccessLevel;
import lombok.Getter;
import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.WeightedPseudograph;

import java.util.*;

@Getter(AccessLevel.PROTECTED)
abstract class GraphBuilder {
  private WeightedGraph<GraphItem, JGraphEdge> graph;
  private List<GraphItem> graphItems;

  public AbstractBaseGraph<GraphItem, JGraphEdge> getJGraphTFrom(ClusteringGraph clusteringGraph) {
    initGraph(clusteringGraph);
    addVertices();
    addEdges(clusteringGraph);
    return (AbstractBaseGraph<GraphItem, JGraphEdge>) graph;
  }

  private void initGraph(ClusteringGraph clusteringGraph) {
    graph = new WeightedPseudograph<>(JGraphEdge.class);
    graphItems = clusteringGraph.getItems();
  }

  private void addVertices() {
    for (GraphItem graphItem : graphItems) {
      graph.addVertex(graphItem);
    }
  }

  protected abstract void addEdges(ClusteringGraph clusteringGraph);

  /**
   * @param parentGraph
   * @return SubGraphs sets, with graph items indices
   */
  public List<Collection<Integer>> getAndUpdateSubGraphs(Graph<GraphItem, JGraphEdge> parentGraph) {
    ConnectivityInspector<GraphItem, JGraphEdge> connectivityInspector = new ConnectivityInspector<>((UndirectedGraph)parentGraph);
    return getSubGraphsIndicesFrom(connectivityInspector.connectedSets());
  }

  private List<Collection<Integer>> getSubGraphsIndicesFrom(List<Set<GraphItem>> subGraphs) {
    List<Collection<Integer>> sets = new ArrayList<>(subGraphs.size());
    for (Set<GraphItem> subGraph : subGraphs) {
      Set<Integer> set = new TreeSet<>();
      sets.add(set);
      for (GraphItem graphItem : subGraph) {
        set.add(graphItem.getVertex1Index());
      }
    }
    return sets;
  }
}
