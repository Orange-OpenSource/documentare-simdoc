package com.orange.documentare.core.comp.clustering.tasksservice;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.graphwriter.GraphWriter;
import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.core.comp.measure.Progress;
import com.orange.documentare.core.comp.measure.ProgressListener;
import com.orange.documentare.core.comp.image.DigitalTypesClustering;
import com.orange.documentare.core.comp.measure.TreatmentStep;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.*;

@Accessors(fluent = true)
@RequiredArgsConstructor
public class ClusteringTask implements ProgressListener {

  @RequiredArgsConstructor
  private class ClusteringResult {
    final ImageSegmentation imageSegmentation;
    final ClusteringGraph clusteringGraph;
  }

  private final String inputFilename;
  private final String outputFilename;
  private final ClusteringParameters clusteringParameters;

  @Setter
  private boolean clearBytes = true;

  private String outputGraphFilename;

  @Setter
  private String strippedOutputFilename;

  @Setter
  private boolean prettyPrint = true;

  @Getter
  private String progressString = "waiting";

  @Getter
  @Setter
  private String error;

  private long t0;

  String id() {
    return outputFilename;
  }

  private File segmentationFile() {
    return new File(inputFilename);
  }

  @Override
  public String toString() {
    return "inputFilename: " + inputFilename + ", outputFilename: " + outputFilename + ", strippedOutputFilename: " + strippedOutputFilename + ", outputGraphFilename: " + outputGraphFilename + ", clusteringParameters: " + clusteringParameters + (error == null ? "" : ", ERROR: " + error);
  }

  public void saveGraphTo(String graphFileName) {
    clearBytes = false;
    outputGraphFilename = graphFileName;
  }

  public void run() throws IOException {
    t0 = System.currentTimeMillis();
    ClusteringResult clusteringResult = builClustering();
    save(clusteringResult);
    progressString = "[DONE] in " + (System.currentTimeMillis() - t0) / 1000 + "s";
  }

  private ClusteringResult builClustering() throws IOException {
    DigitalTypesClustering clustering = new DigitalTypesClustering();
    clustering.setProgressListener(this);
    ObjectMapper mapper = new ObjectMapper();
    ImageSegmentation segmentation = mapper.readValue(new FileInputStream(segmentationFile()), ImageSegmentation.class);
    clustering.setClearDistances(false);
    clustering.setClearBytes(clearBytes);
    clustering.setComputeDistances(!segmentation.isDistancesAvailable());
    ClusteringGraph clusteringGraph = clustering.computeClusterIdsOf(segmentation.getDigitalTypes(), clusteringParameters);
    segmentation.setDistancesAvailable(true);
    segmentation.setClustersAvailable(true);

    return new ClusteringResult(segmentation, clusteringGraph);
  }

  private void save(ClusteringResult clusteringResult) throws IOException {
    ImageSegmentation imageSegmentation = clusteringResult.imageSegmentation;
    imageSegmentation.saveTo(outputFilename, prettyPrint);
    saveGraph(clusteringResult);
    if (strippedOutputFilename != null) {
      saveStrippedJson(imageSegmentation);
    }
  }

  private void saveStrippedJson(ImageSegmentation imageSegmentation) throws IOException {
    imageSegmentation.strip();
    imageSegmentation.saveTo(strippedOutputFilename, prettyPrint);
  }

  private void saveGraph(ClusteringResult clusteringResult) {
    if (outputGraphFilename != null) {
      GraphWriter graphWriter = new GraphWriter(outputGraphFilename, clusteringResult.clusteringGraph, clusteringResult.imageSegmentation.getDigitalTypes());
      graphWriter.write();
    }
  }

  @Override
  public void onProgressUpdate(TreatmentStep step, Progress progress) {
    this.progressString = progress.displayString(step.toString());
  }
}
