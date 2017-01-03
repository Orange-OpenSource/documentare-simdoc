package com.orange.documentare.simdoc.server.biz.clustering;

/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.ClusteringGraphBuilder;
import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.comp.distance.filesdistances.FilesDistances;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.comp.TriangleVertices;
import com.orange.documentare.core.system.filesid.FilesIdBuilder;
import com.orange.documentare.simdoc.server.biz.FileIO;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ClusteringServiceImpl implements ClusteringService {

  @Override
  public ClusteringRequestResult build(
    File inputDirectory, File outputDirectory, ClusteringParameters parameters, boolean debug) throws IOException {

    createSafeInputDirectory(inputDirectory, outputDirectory);
    ClusteringOutput clusteringOutput = buildClustering(inputDirectory, parameters);
    ClusteringRequestResult clusteringRequestResult = prepClusteringRequestResult(inputDirectory, outputDirectory, clusteringOutput);

    FileIO.writeClusteringRequestResult(outputDirectory, clusteringRequestResult);
    if (debug) {
      FileIO.writeClusteringGraph(outputDirectory, clusteringOutput.graph);
    } else {
      FileIO.cleanupClustering(inputDirectory, outputDirectory);
    }

    return clusteringRequestResult;
  }

  private void createSafeInputDirectory(File inputDirectory, File outputDirectory) {
    FilesIdBuilder filesIdBuilder = new FilesIdBuilder();
    filesIdBuilder.createFilesIdDirectory(
      inputDirectory.getAbsolutePath(),
      FileIO.safeInputDir(inputDirectory).getAbsolutePath(),
      outputDirectory.getAbsolutePath());
  }

  private ClusteringOutput buildClustering(File inputDirectory, ClusteringParameters parameters) throws IOException {
    File safeInputDir = FileIO.safeInputDir(inputDirectory);
    FilesDistances filesDistances = FilesDistances.empty();
    filesDistances = filesDistances.compute(safeInputDir, safeInputDir, null);

    SimClusteringItem[] simClusteringItems = initClusteringItems(filesDistances, parameters);
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringGraph graph = clusteringGraphBuilder.build(simClusteringItems, parameters);

    return new ClusteringOutput(simClusteringItems, graph);
  }

  private ClusteringRequestResult prepClusteringRequestResult(File inputDirectory, File outputDirectory, ClusteringOutput clusteringOutput) {
    ClusteringResultItem[] clusteringResultItems =
      ClusteringResultItem.buildItems(inputDirectory, outputDirectory, clusteringOutput.simClusteringItems);
    return ClusteringRequestResult.with(clusteringResultItems);
  }

  private SimClusteringItem[] initClusteringItems(FilesDistances filesDistances, ClusteringParameters parameters) {
    int nbItems = filesDistances.items1.length;
    int k = parameters.knn() ? parameters.kNearestNeighboursThreshold : nbItems;
    SimClusteringItem[] simClusteringItems = new SimClusteringItem[nbItems];
    for(int i = 0; i < nbItems; i++) {
      simClusteringItems[i] = new SimClusteringItem(filesDistances.items1[i].relativeFilename);
    }
    buildTriangulationVertices(simClusteringItems, filesDistances.distancesArray, k);
    return simClusteringItems;
  }

  /** Memory in place creation, it is optimal since we do not allocate nearest arrays */
  private void buildTriangulationVertices(SimClusteringItem[] simClusteringItems, DistancesArray distancesArray, int k) {
    List<SimClusteringItem> itemsList = Arrays.asList(simClusteringItems);
    for (int i = 0; i < simClusteringItems.length; i++) {
      NearestItem vertex2 = distancesArray.nearestItemOf(i);
      NearestItem vertex3 = distancesArray.nearestItemOfBut(vertex2.getIndex(), i);
      simClusteringItems[i].setTriangleVertices(
        new TriangleVertices(distancesArray.nearestItemsFor(itemsList, i), vertex3, k));
    }
  }
}
