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
import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import com.orange.documentare.core.comp.distance.bytesdistances.BytesDistances;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.comp.TriangleVertices;
import com.orange.documentare.core.prepdata.PrepData;
import com.orange.documentare.simdoc.server.biz.FileIO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ClusteringServiceImpl implements ClusteringService {

  @RequiredArgsConstructor
  private class DistancesComputationResult {
    public final String[] ids;
    public final DistancesArray distancesArray;
  }

  @Override
  public ClusteringRequestResult build(FileIO fileIO, ClusteringRequest clusteringRequest) throws IOException {
    ClusteringOutput clusteringOutput = buildClustering(fileIO, clusteringRequest);
    ClusteringRequestResult clusteringRequestResult = clusteringRequest.bytesDataMode ?
      prepClusteringRequestResultInBytesDataMode(clusteringRequest.bytesData, clusteringOutput) :
      prepClusteringRequestResultInFilesMode(fileIO, clusteringOutput);

    fileIO.writeClusteringRequestResult(clusteringRequestResult);
    if (clusteringRequest.debug()) {
      fileIO.writeClusteringGraph(clusteringOutput.graph);
    } else {
      fileIO.cleanupClustering();
    }

    return clusteringRequestResult;
  }

  private void createSafeWorkingDirectory(FileIO fileIO) {
    PrepData prepData = PrepData.builder()
      .inputDirectory(fileIO.inputDirectory())
      .safeWorkingDirConverter()
      .safeWorkingDirectory(fileIO.safeWorkingDirectory())
      .metadataOutputFile(fileIO.metadataFile())
      .build();
    prepData.prep();
  }

  private ClusteringOutput buildClustering(FileIO fileIO, ClusteringRequest clusteringRequest) throws IOException {
    BytesData[] bytesDataArray;
    if (clusteringRequest.bytesDataMode) {
      bytesDataArray = clusteringRequest.bytesData;
    } else {
      createSafeWorkingDirectory(fileIO);
      File safeWorkingDirectory = fileIO.safeWorkingDirectory();
      bytesDataArray = BytesData.loadFromDirectory(safeWorkingDirectory, BytesData.relativePathIdProvider(safeWorkingDirectory));
    }

    DistancesComputationResult distancesComputationResult = computeDistances(bytesDataArray);

    SimClusteringItem[] simClusteringItems = initClusteringItems(distancesComputationResult, clusteringRequest.clusteringParameters());
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringGraph graph = clusteringGraphBuilder.buildGraphAndUpdateClusterIdAndCenter(simClusteringItems, clusteringRequest.clusteringParameters());

    return new ClusteringOutput(simClusteringItems, graph);
  }


  private DistancesComputationResult computeDistances(BytesData[] bytesDataArray) {
    BytesDistances bytesDistances = new BytesDistances();
    DistancesArray distanceArray = bytesDistances.computeDistancesInCollection(bytesDataArray);
    String[] ids = Arrays.stream(bytesDataArray)
      .map(bytesData -> bytesData.id)
      .toArray(String[]::new);
    return new DistancesComputationResult(ids, distanceArray);
  }

  private ClusteringRequestResult prepClusteringRequestResultInFilesMode(FileIO fileIO, ClusteringOutput clusteringOutput) {
    ClusteringResultItem[] clusteringResultItems =
      ClusteringResultItem.buildItemsInFilesMode(fileIO, clusteringOutput.simClusteringItems);
    return ClusteringRequestResult.with(clusteringResultItems);
  }
  private ClusteringRequestResult prepClusteringRequestResultInBytesDataMode(BytesData[] bytesDatas, ClusteringOutput clusteringOutput) {
    ClusteringResultItem[] clusteringResultItems =
      ClusteringResultItem.buildItemsInBytesDataMode(bytesDatas, clusteringOutput.simClusteringItems);
    return ClusteringRequestResult.with(clusteringResultItems);
  }

  private SimClusteringItem[] initClusteringItems(
    DistancesComputationResult distancesComputationResult, ClusteringParameters parameters) {
    String[] ids = distancesComputationResult.ids;
    int nbItems = ids.length;
    int k = parameters.knn() ? parameters.kNearestNeighboursThreshold : nbItems;
    SimClusteringItem[] simClusteringItems = new SimClusteringItem[nbItems];
    for(int i = 0; i < nbItems; i++) {
      simClusteringItems[i] = new SimClusteringItem(ids[i]);
    }
    buildTriangulationVertices(simClusteringItems, distancesComputationResult.distancesArray, k);
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
