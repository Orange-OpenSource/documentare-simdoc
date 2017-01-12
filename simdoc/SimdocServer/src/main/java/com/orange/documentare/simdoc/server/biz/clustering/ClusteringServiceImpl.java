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
import com.orange.documentare.simdoc.server.biz.SharedDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ClusteringServiceImpl implements ClusteringService {

  @Override
  public ClusteringRequestResult build(FileIO fileIO, ClusteringParameters parameters, boolean debug) throws IOException {
    createSafeInputDirectory(fileIO);
    ClusteringOutput clusteringOutput = buildClustering(fileIO, parameters);
    ClusteringRequestResult clusteringRequestResult = prepClusteringRequestResult(fileIO, clusteringOutput);

    fileIO.writeClusteringRequestResult(clusteringRequestResult);
    if (debug) {
      fileIO.writeClusteringGraph(clusteringOutput.graph);
    } else {
      fileIO.cleanupClustering();
    }

    return clusteringRequestResult;
  }

  private void createSafeInputDirectory(FileIO fileIO) {
    FilesIdBuilder filesIdBuilder = new FilesIdBuilder();
    filesIdBuilder.createFilesIdDirectory(
      fileIO.inPath(),
      fileIO.safeInputDir().getAbsolutePath(),
      fileIO.outPath());
  }

  private ClusteringOutput buildClustering(FileIO fileIO, ClusteringParameters parameters) throws IOException {
    File safeInputDir = fileIO.safeInputDir();
    FilesDistances filesDistances = FilesDistances.empty();
    filesDistances = filesDistances.compute(safeInputDir, safeInputDir, null);

    SimClusteringItem[] simClusteringItems = initClusteringItems(filesDistances, parameters);
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringGraph graph = clusteringGraphBuilder.build(simClusteringItems, parameters);

    return new ClusteringOutput(simClusteringItems, graph);
  }

  private ClusteringRequestResult prepClusteringRequestResult(FileIO fileIO, ClusteringOutput clusteringOutput) {
    ClusteringResultItem[] clusteringResultItems =
      ClusteringResultItem.buildItems(fileIO, clusteringOutput.simClusteringItems);
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
