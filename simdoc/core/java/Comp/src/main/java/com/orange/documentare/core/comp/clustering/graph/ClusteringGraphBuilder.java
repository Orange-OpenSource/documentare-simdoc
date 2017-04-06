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

import com.orange.documentare.core.comp.clustering.graph.check.CheckGraph;
import com.orange.documentare.core.comp.clustering.graph.clusters.ClusterTreatments;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.JGraphEdge;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.JGraphTBuilder;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.SubgraphsBuilder;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.TriangulationGraphBuilder;
import com.orange.documentare.core.comp.clustering.graph.subgraphs.SubGraphTreatments;
import com.orange.documentare.core.comp.clustering.graph.voronoi.Voronoi;
import com.orange.documentare.core.comp.measure.ProgressListener;
import com.orange.documentare.core.comp.measure.TreatmentStep;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import com.orange.documentare.core.system.measure.Progress;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;

import java.util.List;

/**
 * For each clustering item, computes the triangle surface and equilaterality factor
 * of this item with its first two nearests elements, then build the associated graph.
 */
@Slf4j
public class ClusteringGraphBuilder {
  private ClusteringItem[] items;
  private ClusteringParameters parameters;


  @Setter
  private ProgressListener progressListener;

  private long t0;
  private int percent;

  public ClusteringGraph build(ClusteringItem[] items, ClusteringParameters parameters) {
    this.items = items;
    this.parameters = parameters;
    log.info(parameters.toString());
    t0 = System.currentTimeMillis();
    ClusteringGraph clusteringGraph = doBuild();
    percent = 100;
    onProgress(TreatmentStep.DONE);
    return clusteringGraph;
  }

  private ClusteringGraph doBuild() {
    ClusteringGraph clusteringGraph = new ClusteringGraph();
    SubgraphsBuilder subgraphsBuilder = new SubgraphsBuilder(clusteringGraph);
    triangulationTreatments(clusteringGraph.getItems());
    subgraphsBuilder.computeSubGraphs(new TriangulationGraphBuilder());
    subGraphsPostTreatments(clusteringGraph);
    rebuildSubGraphsAndClusters(clusteringGraph, subgraphsBuilder, items);
    clustersPostTreatments(clusteringGraph, subgraphsBuilder);
    check(clusteringGraph);
    return clusteringGraph;
  }

  private void triangulationTreatments(List<GraphItem> graphItems) {
    onProgress(TreatmentStep.TRIANGULATION);
    int kNearestNeighboursThreshold = parameters.knn() ? parameters.kNearestNeighboursThreshold : items.length;
    GraphItemsBuilder graphItemsBuilder = new GraphItemsBuilder(items, graphItems, kNearestNeighboursThreshold);
    graphItemsBuilder.initGraphItems();
    TriangulationTreatments triangulationTreatments = new TriangulationTreatments(graphItems, parameters);
    triangulationTreatments.doTreatments();
  }

  private void subGraphsPostTreatments(ClusteringGraph clusteringGraph) {
    percent = 10;
    onProgress(TreatmentStep.SUBGRAPHS_POST_PROCESSING);
    SubGraphTreatments subGraphTreatments = new SubGraphTreatments(clusteringGraph, parameters);
    subGraphTreatments.doTreatments();
  }

  private void clustersPostTreatments(ClusteringGraph clusteringGraph, SubgraphsBuilder subgraphsBuilder) {
    percent = 50;
    onProgress(TreatmentStep.CLUSTERS_POST_PROCESSING);
    ClusterTreatments clusterTreatments = new ClusterTreatments(clusteringGraph, parameters, items);
    if (parameters.ccut()) {
      clusterTreatments.cutLongestVertices();
      rebuildSubGraphsAndClusters(clusteringGraph, subgraphsBuilder, items);
    }
    clusterTreatments.updateClusterCenter();
  }

  private void check(ClusteringGraph clusteringGraph) {
    percent = 90;
    onProgress(TreatmentStep.CHECK_GRAPH);
    CheckGraph checkGraph = new CheckGraph(clusteringGraph);
    checkGraph.check();
    log.info("Graph check: OK");
  }

  private void rebuildSubGraphsAndClusters(ClusteringGraph clusteringGraph, SubgraphsBuilder subgraphsBuilder, ClusteringItem[] items) {
    Graph<GraphItem, JGraphEdge> graph;
    graph = subgraphsBuilder.computeSubGraphs(new JGraphTBuilder());;
    buildVoronoiClusters(clusteringGraph, items, graph);
  }



  /** Use voronoi algo to detect graph regions, which may generate new subgraphs
   * @param clusteringGraph
   * @param items*/
  private void buildVoronoiClusters(ClusteringGraph clusteringGraph, ClusteringItem[] items, Graph<GraphItem, JGraphEdge> graph) {
    onProgress(TreatmentStep.VORONOI);
    clusteringGraph.getClusters().clear();
    Voronoi voronoi = new Voronoi(items, clusteringGraph, graph);
    voronoi.mapClusterId();
    log.info("Voronoi, subgraphs = {}, clusters = {}", clusteringGraph.getSubGraphs().size(), clusteringGraph.getClusters().values().size());
  }

  private void onProgress(TreatmentStep step) {
    if (progressListener != null) {
      progressListener.onProgressUpdate(step, new Progress(percent, (int) (System.currentTimeMillis() - t0) / 1000));
    }
  }
}
