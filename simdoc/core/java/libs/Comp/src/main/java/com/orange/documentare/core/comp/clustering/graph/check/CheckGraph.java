package com.orange.documentare.core.comp.clustering.graph.check;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.clustering.graph.GraphCluster;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import com.orange.documentare.core.model.ref.clustering.graph.SubGraph;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CheckGraph {
  private final ClusteringGraph clusteringGraph;

  public void check() {
    itemsFoundOnce();
    subgraphsAndClustersCoherency();
  }

  private void itemsFoundOnce() {
    for (GraphItem graphItem : clusteringGraph.getItems()) {
      int itemIndex = graphItem.getVertex1Index();
      itemFoundOnce(itemIndex);
    }
  }

  private void itemFoundOnce(int itemIndex) {
    itemFoundOnceInSubgraphs(itemIndex);
    itemFoundOnceInClusters(itemIndex);
  }

  private void itemFoundOnceInSubgraphs(int itemIndex) {
    int count = 0;
    for (SubGraph subGraph : clusteringGraph.getSubGraphs().values()) {
      if (subGraph.getItemIndices().contains(itemIndex)) {
        count++;
      }
    }
    if (count != 1) {
      throwNewException(String.format("Item %d found %d times in subgraphs", itemIndex, count));
    }
  }

  private void itemFoundOnceInClusters(int itemIndex) {
    int count = 0;
    for (GraphCluster cluster : clusteringGraph.getClusters().values()) {
      if (cluster.getItemIndices().contains(itemIndex)) {
        count++;
      }
    }
    if (count != 1) {
      throwNewException(String.format("Item %d found %d times in clusters", itemIndex, count));
    }
  }

  private void subgraphsAndClustersCoherency() {
    Collection<SubGraph> subgraphs = clusteringGraph.getSubGraphs().values();
    Collection<GraphCluster> clusters = clusteringGraph.getClusters().values();
    checkSize(subgraphs, clusters);
    checkClustersInSubGraphs(subgraphs);
    checkClustersSubGraph(clusters);
    checkItemsInSubGraphAndCluster();
  }

  private void checkSize(Collection<SubGraph> subgraphs, Collection<GraphCluster> clusters) {
    if (subgraphs.size() > clusters.size()) {
      throwNewException(String.format("Less clusters (%d) than subgraphs (%d)", clusters.size(), subgraphs.size()));
    }
  }

  private void checkClustersInSubGraphs(Collection<SubGraph> subgraphs) {
    for (SubGraph subGraph : subgraphs) {
      checkClustersInSubGraph(subGraph);
    }
  }

  private void checkClustersInSubGraph(SubGraph subGraph) {
    Map<Integer, GraphCluster> clusters = clusteringGraph.getClusters();
    for (int clusterIndex : subGraph.getClusterIndices()) {
      if (clusters.get(clusterIndex).getSubgraphId() != subGraph.getGroupId()) {
        throwNewException(String.format("cluster (index = %d) and subgraph (id = %s) are not coherent", clusterIndex, subGraph.getGroupId()));
      }
    }
  }

  private void checkClustersSubGraph(Collection<GraphCluster> clusters) {
    for (GraphCluster graphCluster : clusters) {
      checkClustersSubGraph(graphCluster);
    }
  }

  private void checkClustersSubGraph(GraphCluster graphCluster) {
    SubGraph subgraph = clusteringGraph.getSubGraphs().get(graphCluster.getSubgraphId());
    if (!subgraph.getClusterIndices().contains(graphCluster.getGroupId())) {
      throwNewException(String.format("cluster (id = %d) and subgraph (id = %s) are not coherent", graphCluster.getGroupId(), subgraph.getGroupId()));
    }
  }

  private void checkItemsInSubGraphAndCluster() {
    for (GraphItem graphItem : clusteringGraph.getItems()) {
      checkItemInSubGraphAndCluster(graphItem);
    }
  }

  private void checkItemInSubGraphAndCluster(GraphItem graphItem) {
    int itemIndex = graphItem.getVertex1Index();
    if (!clusteringGraph.getClusters().get(graphItem.getClusterId()).getItemIndices().contains(itemIndex)) {
      throwNewException(String.format("item (index = %d) not found in cluster (id = %d)", itemIndex, graphItem.getClusterId()));
    }
    if (!clusteringGraph.getSubGraphs().get(graphItem.getSubgraphId()).getItemIndices().contains(itemIndex)) {
      throwNewException(String.format("item (index = %d) not found in subgraph (id = %d)", itemIndex, graphItem.getSubgraphId()));
    }
  }

  private void throwNewException(String message) {
    throw new CheckGraphException(message);
  }
}
