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
import com.orange.documentare.simdoc.server.biz.FileIO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    ClusteringOutput clusteringOutput = buildClustering(clusteringRequest);

    ClusteringRequestResult clusteringRequestResult = prepClusteringRequestResultInBytesDataMode(clusteringRequest.bytesData, clusteringOutput, fileIO);

    fileIO.writeClusteringRequestResult(clusteringRequestResult);
    if (clusteringRequest.debug()) {
      fileIO.writeClusteringGraph(clusteringOutput.graph);
    }
    return clusteringRequestResult;
  }

  private ClusteringOutput buildClustering(ClusteringRequest clusteringRequest) throws IOException {

    DistancesComputationResult distancesComputationResult = computeDistances(clusteringRequest.bytesData);

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


  private ClusteringRequestResult prepClusteringRequestResultInBytesDataMode(BytesData[] bytesData, ClusteringOutput clusteringOutput, FileIO fileIO) {
    ClusteringResultItem[] clusteringResultItems =
      ClusteringResultItem.buildItemsInBytesDataModeWithFilesPreparation(bytesData, clusteringOutput.simClusteringItems, fileIO);
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
      NearestItem[] vertex1Nearest = distancesArray.nearestItemsFor(itemsList, i);

      // FIXME we keep nearest items for singletons experimentation on Jo's side
      simClusteringItems[i].setNearestItems(vertex1Nearest);

      simClusteringItems[i].setTriangleVertices(
        new TriangleVertices(vertex1Nearest, vertex3, k));
    }
  }
}
