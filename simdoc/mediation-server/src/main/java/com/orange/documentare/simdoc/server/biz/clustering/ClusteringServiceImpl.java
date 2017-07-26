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
import com.orange.documentare.core.image.opencv.OpencvLoader;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.comp.TriangleVertices;
import com.orange.documentare.core.prepdata.PrepData;
import com.orange.documentare.simdoc.server.biz.FileIO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ClusteringServiceImpl implements ClusteringService {

  @Value("${clustering.server.url}")
  private String clusteringServerUrl;

  static {
    OpencvLoader.load();
  }

  @RequiredArgsConstructor
  private class DistancesComputationResult {
    public final String[] ids;
    public final DistancesArray distancesArray;
  }

  @Override
  public ClusteringRequestResult build(FileIO fileIO, ClusteringRequest clusteringRequest) throws IOException {
    // Raw result on prepped files
    ClusteringRequestResult clusteringRequestResultInternal = buildRemoteClustering(fileIO, clusteringRequest);

    // Remap result on input files
    ClusteringRequestResult remapResult = clusteringRequest.bytesDataMode() ?
      prepClusteringRequestResultInBytesDataMode(clusteringRequest.bytesData, clusteringRequestResultInternal, fileIO) :
      prepClusteringRequestResultInFilesMode(fileIO, clusteringRequestResultInternal);

    fileIO.writeClusteringRequestResult(remapResult);
    if (!clusteringRequest.debug()) {
      fileIO.cleanupClustering();
    }

    return remapResult;
  }

  private ClusteringRequestResult buildRemoteClustering(FileIO fileIO, ClusteringRequest mediationRequest) throws IOException {
    BytesData[] bytesDataArray = prepData(fileIO, mediationRequest);

    ClusteringRequest.ClusteringRequestBuilder clusteringRequestBuilder = ClusteringRequest.builder()
      .bytesData(bytesDataArray)
      .outputDirectory(mediationRequest.outputDirectory);

    if (mediationRequest.acutSdFactor != null) {
      clusteringRequestBuilder.acut(mediationRequest.acutSdFactor);
    }

    if (mediationRequest.ccutPercentile != null) {
      clusteringRequestBuilder.ccut(mediationRequest.ccutPercentile);
    }

    if (mediationRequest.qcutSdFactor != null) {
      clusteringRequestBuilder.qcut(mediationRequest.qcutSdFactor);
    }

    if (mediationRequest.scutSdFactor != null) {
      clusteringRequestBuilder.scut(mediationRequest.scutSdFactor);
    }


    if ((mediationRequest.wcut != null) && (mediationRequest.wcut == true)) {
      clusteringRequestBuilder.wcut();
    }

    if ((mediationRequest.debug != null) && (mediationRequest.debug == true)) {
      clusteringRequestBuilder.debug();
    }

    if ((mediationRequest.sloop != null) && (mediationRequest.sloop == true)) {
      clusteringRequestBuilder.sloop();
    }

    ClusteringRequest clusteringRequest = clusteringRequestBuilder
      .build();

    RemoteClustering remoteClustering = new RemoteClustering();
    String url = clusteringServerUrl;
    ClusteringRequestResult clusteringRequestResult = remoteClustering.request(url, clusteringRequest);

    return clusteringRequestResult;
  }


  private BytesData[] prepData(FileIO fileIO, ClusteringRequest clusteringRequest) {
    // if bytes are already loaded, there is no directory to prep
    boolean nothingToPrep = clusteringRequest.bytesDataMode() && clusteringRequest.bytesData[0].bytes != null;
    if (nothingToPrep) {
      return clusteringRequest.bytesData;
    }
    createSafeWorkingDirectory(fileIO, clusteringRequest.bytesData);
    File safeWorkingDirectory = fileIO.safeWorkingDirectory();
    return BytesData.loadFromDirectory(safeWorkingDirectory, BytesData.relativePathIdProvider(safeWorkingDirectory));
  }

  private void createSafeWorkingDirectory(FileIO fileIO, BytesData[] bytesData) {
    PrepData prepData = PrepData.builder()
      .inputDirectory(fileIO.inputDirectory()) // may be null
      .bytesData(bytesData) // may be null
      .withRawConverter(true)
      .expectedRawBytesCount(1024*1024)
      .safeWorkingDirConverter()
      .safeWorkingDirectory(fileIO.safeWorkingDirectory())
      .metadataOutputFile(fileIO.metadataFile())
      .build();
    prepData.prep();
  }



  private ClusteringRequestResult prepClusteringRequestResultInFilesMode(FileIO fileIO, ClusteringRequestResult clusteringRequestResultInternal) {
    ClusteringResultItem[] clusteringResultItems =
      ClusteringResultItem.buildItemsInFilesMode(fileIO, clusteringRequestResultInternal.clustering);
    return ClusteringRequestResult.with(clusteringResultItems);
  }
  private ClusteringRequestResult prepClusteringRequestResultInBytesDataMode(BytesData[] bytesData, ClusteringRequestResult clusteringRequestResultInternal, FileIO fileIO) {
    ClusteringResultItem[] clusteringResultItems =
      ClusteringResultItem.buildItemsInBytesDataModeWithFilesPreparation(bytesData, clusteringRequestResultInternal.clustering, fileIO);
    return ClusteringRequestResult.with(clusteringResultItems);
  }



}
