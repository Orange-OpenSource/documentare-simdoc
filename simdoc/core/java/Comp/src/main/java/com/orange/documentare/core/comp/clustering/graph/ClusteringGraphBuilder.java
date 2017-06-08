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
import org.apache.commons.io.FileUtils;
import org.jgrapht.Graph;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * For each clustering item, computes the triangle surface and equilaterality factor
 * of this item with its first two nearests elements, then buildGraphAndUpdateClusterIdAndCenter the associated graph.
 */
@Slf4j
public class ClusteringGraphBuilder {
  private ClusteringParameters parameters;

  @Setter
  private ProgressListener progressListener;

  private long t0;
  private int percent;

  public ClusteringGraph buildGraphAndUpdateClusterIdAndCenter(ClusteringItem[] clusteringItems, ClusteringParameters parameters) {
    this.parameters = parameters;
    log.info(parameters.toString());
    t0 = System.currentTimeMillis();

    ClusteringGraph clusteringGraph = doBuild(clusteringItems);
    updateClusterIdAndCenter(clusteringItems, clusteringGraph.getItems());

    ClusteringItem[] singletons = retrieveSingletonsItemsFrom(clusteringItems, clusteringGraph);
    SingletonForReGraph[] singletonsCopy = buildSingletonsForRegraph(clusteringItems, singletons);

    ClusteringGraph clusteringGraphFromSingletons = doBuild(singletonsCopy);
    updateClusterIdAndCenter(singletonsCopy, clusteringGraphFromSingletons.getItems());

/*
    ClusteringItem[] singletons = retrieveSingletonsItemsFrom(clusteringItems, clusteringGraph);
    ClusteringGraph clusteringGraphFromSingletons = doBuild(singletons);
    updateClusterIdAndCenter(clusteringItems, clusteringGraphFromSingletons.getItems());

    ClusteringGraph mergedGraphs = merge(clusteringGraph, clusteringGraphFromSingletons);
*/
    percent = 100;
    onProgress(TreatmentStep.DONE);
    //return mergedGraphs;
    return clusteringGraph;
  }

  private SingletonForReGraph[] buildSingletonsForRegraph(ClusteringItem[] clusteringItems, ClusteringItem[] singletons) {
    List<ClusteringItem> originalItemsList = Arrays.asList(clusteringItems);
    Map<Integer, Integer> singletonsOldToNewIndexMap = new HashMap<>();
    IntStream.range(0, singletons.length).forEach(i ->
      singletonsOldToNewIndexMap.put(originalItemsList.indexOf(singletons[i]), i)
    );

    List<SingletonForReGraph> singletonsCopy = Arrays.stream(singletons)
      .map(singleton -> new SingletonForReGraph(singleton, originalItemsList.indexOf(singleton), singletonsOldToNewIndexMap))
      .collect(Collectors.toList());

    return singletonsCopy.toArray(new SingletonForReGraph[singletonsCopy.size()]);
  }

  private ClusteringGraph merge(ClusteringGraph clusteringGraph, ClusteringGraph clusteringGraphFromSingletons) {
    return null;
  }

  ClusteringItem[] retrieveSingletonsItemsFrom(ClusteringItem[] clusteringItems, ClusteringGraph clusteringGraph) {
    List<ClusteringItem> singletonList = clusteringGraph.getSubGraphs().values().stream()
      .filter(subGraph -> subGraph.getItemIndices().size() == 1)
      .map(subGraph -> subGraph.getItemIndices().get(0))
      .map(singletonIndex -> clusteringItems[singletonIndex])
      .collect(Collectors.toList());

    // FIXME: write singleton name in text file for Jojo debug Please remove me
    String singletonText = "";
    for(ClusteringItem clusteringItem:singletonList) {
      singletonText = singletonText + clusteringItem.getHumanReadableId() + '\n';
    }
    try {
      FileUtils.writeStringToFile(new File("singleton.txt"), singletonText);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return singletonList.toArray(new ClusteringItem[singletonList.size()]);
  }


  private ClusteringGraph doBuild(ClusteringItem[] clusteringItems) {
    List<GraphItem> graphItems = buildGraphItems(clusteringItems);
    ClusteringGraph clusteringGraph = new ClusteringGraph(graphItems);
    SubgraphsBuilder subgraphsBuilder = new SubgraphsBuilder(clusteringGraph);

    triangulationTreatments(graphItems);
    subgraphsBuilder.computeSubGraphs(new TriangulationGraphBuilder());

    subGraphsTreatments(clusteringGraph, subgraphsBuilder);

    clustersPostTreatments(clusteringGraph, subgraphsBuilder);
    check(clusteringGraph);

    return clusteringGraph;
  }

  /** if sloop is false, call treatment once, otherwise it loops */
  private void subGraphsTreatments(ClusteringGraph clusteringGraph, SubgraphsBuilder subgraphsBuilder) {
    // FIXME: increment percent in sloop?
    percent = 10;
    onProgress(TreatmentStep.SUBGRAPHS_POST_PROCESSING);

    int sloopLoops = 0;
    float variableScut = parameters.scutSdFactor;
    int subgraphNb;
    int clusterNb;
    do {
      doSubGraphTreatments(clusteringGraph, subgraphsBuilder, variableScut);

      subgraphNb = clusteringGraph.getSubGraphs().size();
      clusterNb = clusteringGraph.getClusters().values().size();

      variableScut -= 0.10;
      sloopLoops++;
    } while (parameters.sloop && subgraphNb != clusterNb && variableScut > 0);

    log.info("Subgraphs treatments, subgraphs = {}, clusters = {}, sloop({}), slooploops({})", subgraphNb, clusterNb, parameters.sloop, sloopLoops);
  }

  private void doSubGraphTreatments(ClusteringGraph clusteringGraph, SubgraphsBuilder subgraphsBuilder, float scutSdFactor) {
    SubGraphTreatments subGraphTreatments = new SubGraphTreatments(clusteringGraph, parameters);
    subGraphTreatments.doTreatments(scutSdFactor);
    rebuildSubGraphsAndClusters(clusteringGraph, subgraphsBuilder);
  }


  private void triangulationTreatments(List<GraphItem> graphItems) {
    onProgress(TreatmentStep.TRIANGULATION);

    TriangulationTreatments triangulationTreatments = new TriangulationTreatments(graphItems, parameters);
    triangulationTreatments.doTreatments();
  }

  private List<GraphItem> buildGraphItems(ClusteringItem[] clusteringItems) {
    int kNearestNeighboursThreshold = parameters.knn() ? parameters.kNearestNeighboursThreshold : clusteringItems.length;
    GraphItemsBuilder graphItemsBuilder = new GraphItemsBuilder(clusteringItems, kNearestNeighboursThreshold);
    return graphItemsBuilder.initGraphItems();
  }

  private void clustersPostTreatments(ClusteringGraph clusteringGraph, SubgraphsBuilder subgraphsBuilder) {
    percent = 50;
    onProgress(TreatmentStep.CLUSTERS_POST_PROCESSING);
    ClusterTreatments clusterTreatments = new ClusterTreatments(clusteringGraph, parameters);
    if (parameters.ccut()) {
      clusterTreatments.cutLongestVertices();
      rebuildSubGraphsAndClusters(clusteringGraph, subgraphsBuilder);
    }
    clusterTreatments.updateClusterCenter();

    log.info("Clusters treatments, subgraphs = {}, clusters = {}", clusteringGraph.getSubGraphs().size(), clusteringGraph.getClusters().values().size());
  }

  private void check(ClusteringGraph clusteringGraph) {
    percent = 90;
    onProgress(TreatmentStep.CHECK_GRAPH);
    CheckGraph checkGraph = new CheckGraph(clusteringGraph);
    checkGraph.check();
    log.info("Graph check: OK");
  }

  private void rebuildSubGraphsAndClusters(ClusteringGraph clusteringGraph, SubgraphsBuilder subgraphsBuilder) {
    Graph<GraphItem, JGraphEdge> graph;
    graph = subgraphsBuilder.computeSubGraphs(new JGraphTBuilder());;
    buildVoronoiClusters(clusteringGraph, graph);
  }




  /** Use voronoi algo to detect graph regions, which may generate new subgraphs */
  private void buildVoronoiClusters(ClusteringGraph clusteringGraph, Graph<GraphItem, JGraphEdge> graph) {
    onProgress(TreatmentStep.VORONOI);
    clusteringGraph.getClusters().clear();
    Voronoi voronoi = new Voronoi(clusteringGraph, graph);
    voronoi.mapClusterId();
  }

  private void onProgress(TreatmentStep step) {
    if (progressListener != null) {
      progressListener.onProgressUpdate(step, new Progress(percent, (int) (System.currentTimeMillis() - t0) / 1000));
    }
  }

  private void updateClusterIdAndCenter(ClusteringItem[] clusteringItems, List<GraphItem> graphItems) {
    for (int i = 0; i < graphItems.size(); i++) {
      ClusteringItem clusteringItem = clusteringItems[i];
      GraphItem graphItem = graphItems.get(i);
      clusteringItem.setClusterId(graphItem.getClusterId());
      clusteringItem.setClusterCenter(graphItem.isClusterCenter());
    }
  }
}
