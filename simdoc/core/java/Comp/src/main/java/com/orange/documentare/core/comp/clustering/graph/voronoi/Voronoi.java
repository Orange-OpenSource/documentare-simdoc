package com.orange.documentare.core.comp.clustering.graph.voronoi;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.GraphvizPath;
import com.orange.documentare.core.comp.clustering.graph.clusters.SameCluster;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.GroupEdges;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.JGraphEdge;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.SameGroup;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.dotexport.DOT;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.dotexport.ExportVertexNameProvider;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphCluster;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import com.orange.documentare.core.system.nativeinterface.NativeInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jgrapht.Graph;
import org.jgrapht.ext.DOTExporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
/**
 * Voronoi maps.
 * NB: concurrent access are handled with UUID.
 */
public class Voronoi {
  private final ClusteringItem[] clusteringItems;
  private final ClusteringGraph clusteringGraph;
  private final Graph<GraphItem, JGraphEdge> graph;
  private final String graphDotIn;
  private final String graphDotOut;
  private final String cmdFilterLog;

  public Voronoi(ClusteringItem[] clusteringItems, ClusteringGraph clusteringGraph, Graph<GraphItem, JGraphEdge> graph) {
    this.clusteringItems = clusteringItems;
    this.clusteringGraph = clusteringGraph;
    this.graph = graph;
    String sessionId = getSessionId();
    graphDotIn = "/tmp/tmp_graph_in_" + sessionId + ".dot";
    graphDotOut = "/tmp/tmp_graph_out_" + sessionId + ".dot";
    cmdFilterLog = "/tmp/tmp_graph_" + sessionId + ".log";
  }

  private String getSessionId() {
    return UUID.randomUUID().toString();
  }

  public void mapClusterId() {
    exportGraph();
    NativeInterface.launch(getGraphVizCmd(), null, cmdFilterLog);
    importGraph();
    updateClusterEdges();
  }

  private void exportGraph() {
    DOTExporter<GraphItem, JGraphEdge> dotExporter = new DOTExporter<>(new ExportVertexNameProvider(), null, null);
    try {
      dotExporter.export(new FileWriter(graphDotIn), graph);
    } catch (IOException e) {
      log.error("Voronoi treatment failed", e);
      throw new VoronoiTreatmentException(e.getMessage());
    }
  }

  private String getGraphVizCmd() {
    return String.format(GraphvizPath.PATH + "sfdp %s | " + GraphvizPath.PATH + "gvmap -e -o %s && grep -E 'g_.*\\[|cluster=' %s | grep -v '\\-\\-' | grep -v 'cluster=-1' | sed \"s/\\[//\" | sed \"s/cluster=//\" | sed \"s/,//\"", graphDotIn, graphDotOut, graphDotOut);
  }

  private void importGraph() {
    try {
      String importString = getImportString();
      updateItemsClusterId(importString);
    } catch (IOException e2) {
      throwVoronoiException(e2);
    }
  }

  private String getImportString() throws IOException {
    return FileUtils.readFileToString(new File(cmdFilterLog));
  }

  private void updateItemsClusterId(String importString) {
    importString = importString.replace("\t", "");
    String[] strings = importString.split("\\s");
    for (int i = 0; i < strings.length; i+=2) {
      int clusterId = parseClusterId(strings[i + 1]);
      setClusterIdWith(strings[i], clusterId);
    }
  }

  private void setClusterIdWith(String vertextDotName, int clusterId) {
    ClusteringItem clusteringItem = findClusteringItemMatching(vertextDotName);
    clusteringItem.setClusterId(clusterId);
    GraphItem graphItem = findGraphItemMatching(vertextDotName);
    graphItem.setClusterId(clusterId);
    linkClusterAndSubGraph(graphItem);
  }

  private int parseClusterId(String clusterIdString) {
    try {
      return Integer.parseInt(clusterIdString);
    } catch (NumberFormatException e) {
      throwVoronoiException(e);
      return -1;
    }
  }

  private ClusteringItem findClusteringItemMatching(String vertextDotName) {
    for (ClusteringItem clusteringItem : clusteringItems) {
      if (vertextDotName.equals(DOT.getDOTVertexName(clusteringItem.getHumanReadableId()))) {
        return clusteringItem;
      }
    }
    throwVoronoiException(new IllegalStateException("Failed to find mathing item in graphviz output"));
    return null;
  }

  private GraphItem findGraphItemMatching(String vertextDotName) {
    for (GraphItem graphItem : clusteringGraph.getItems()) {
      if (vertextDotName.equals(DOT.getDOTVertexName(graphItem.getVertexName()))) {
        return graphItem;
      }
    }
    throwVoronoiException(new IllegalStateException("Failed to find mathing item in graphviz output"));
    return null;
  }

  private void linkClusterAndSubGraph(GraphItem graphItem) {
    GraphCluster graphCluster = getGraphCluster(graphItem);
    checkCluster(graphCluster, graphItem.getSubgraphId(), graphItem.getClusterId());
    addItemIndexToCluster(graphCluster, graphItem.getVertex1Index());
  }

  private void checkCluster(GraphCluster graphCluster, int subgraphId, int clusterId) {
    if (graphCluster.getSubgraphId() != subgraphId) {
      throwVoronoiException(new IllegalStateException("items in same cluster but with different subgraph id..."));
    }
    if (graphCluster.getGroupId() != clusterId) {
      throwVoronoiException(new IllegalStateException("items in same cluster but with different cluster id..."));
    }
  }

  private void addItemIndexToCluster(GraphCluster graphCluster, int itemIndex) {
    List<Integer> itemIndices = graphCluster.getItemIndices();
    if (itemIndices.contains(itemIndex)) {
      throwVoronoiException(new IllegalStateException("Cluster already contains item"));
    } else {
      itemIndices.add(itemIndex);
    }
  }

  private GraphCluster getGraphCluster(GraphItem graphItem) {
    Map<Integer, GraphCluster> clusters = clusteringGraph.getClusters();
    GraphCluster graphCluster = clusters.get(graphItem.getClusterId());
    if (graphCluster == null) {
      return newGraphCLuster(graphItem);
    }
    return graphCluster;
  }

  private GraphCluster newGraphCLuster(GraphItem graphItem) {
    GraphCluster graphCluster = new GraphCluster();
    int clusterId = graphItem.getClusterId();
    int subgraphId = graphItem.getSubgraphId();
    graphCluster.setGroupId(clusterId);
    graphCluster.setSubgraphId(subgraphId);
    addNewCluster(graphCluster, clusterId, subgraphId);
    return graphCluster;
  }

  private void addNewCluster(GraphCluster graphCluster, int clusterId, int subgraphId) {
    clusteringGraph.getSubGraphs().get(subgraphId).getClusterIndices().add(clusterId);
    if (clusteringGraph.getClusters().containsKey(clusterId)) {
      throwVoronoiException(new IllegalStateException("A cluster with same id already exists"));
    } else {
      clusteringGraph.getClusters().put(clusterId, graphCluster);
    }
  }

  private void updateClusterEdges() {
    SameGroup sameGroup = new SameCluster();
    GroupEdges groupEdges = new GroupEdges(clusteringGraph.getItems(), clusteringGraph.getClusters(), sameGroup);
    groupEdges.updateGroupsEdges(graph.edgeSet());
  }

  private void throwVoronoiException(Exception e) {
    log.error("Voronoi treatment failed", e);
    throw new VoronoiTreatmentException(e.getMessage());
  }
}
