package com.orange.documentare.app.simclustering;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orange.documentare.app.simclustering.cmdline.CommandLineOptions;
import com.orange.documentare.core.comp.clustering.graph.ClusteringGraphBuilder;
import com.orange.documentare.core.comp.clustering.graph.scissors.ClusteringParameters;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.common.CommandLineException;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.*;


import java.io.File;
import java.io.IOException;

@Log4j2
public class SimClustering {
  private static final File SIMDOC_EXPORT_FILE = new File("sc_segmentation_ready_for_user_interface.json.gz");
  private static final File GRAPH_OUTPUT = new File("sc_graph_input.json.gz");

  private static CommandLineOptions options;

  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
    try {
      options = new CommandLineOptions(args);
      doTheJob(options);
    } catch (CommandLineException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void doTheJob(CommandLineOptions options) throws IOException {
    if (options.getSimDocJson() != null) {
      doTheJobForSimDoc(options.getSimDocJson());
    } else {
      doTheJobForRegularFiles(options);
    }
  }

  private static void doTheJobForSimDoc(File simDocJson) throws IOException {
    ImageSegmentation imageSegmentation = segmentationOf(simDocJson);
    DigitalTypes copyWithoutSpaces = imageSegmentation.getDigitalTypes().copyWithoutSpaces();
    ClusteringItem[] items = copyWithoutSpaces.toArray(new DigitalType[copyWithoutSpaces.size()]);
    computeClustering(items);
    SimDocExport export = new SimDocExport(imageSegmentation);
    export.exportTo(SIMDOC_EXPORT_FILE);
  }

  private static ImageSegmentation segmentationOf(File simDocJson) throws IOException {
    JsonGenericHandler jsonHandler = new JsonGenericHandler(true);
    return (ImageSegmentation) jsonHandler.getObjectFromJsonGzipFile(ImageSegmentation.class, simDocJson);
  }

  private static void doTheJobForRegularFiles(CommandLineOptions options) throws IOException {
    InputItem[] inputItems = getInputItemsFrom(options.getDistancesJsonFile());
    computeClustering(inputItems);
  }

  private static InputItem[] getInputItemsFrom(File jsonFile) throws IOException {
    JsonGenericHandler jsonHandler = new JsonGenericHandler(true);
    log.info("Start unzip...");
    ImportModel importModel = (ImportModel) jsonHandler.getObjectFromJsonGzipFile(ImportModel.class, jsonFile);
    log.info("...done");
    return importModel.getItems();
  }

  private static void computeClustering(ClusteringItem[] items) throws IOException {
    ClusteringGraph clusteringGraph = getClusteringGraphFor(items);
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    jsonGenericHandler.getMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    jsonGenericHandler.writeObjectToJsonGzipFile(clusteringGraph, GRAPH_OUTPUT);
  }

  private static ClusteringGraph getClusteringGraphFor(ClusteringItem[] items) {
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters parameters = new ClusteringParameters();
    updateParameters(parameters);
    return clusteringGraphBuilder.build(items, parameters);
  }

  private static void updateParameters(ClusteringParameters parameters) {
    float qStdFactor = options.getQStdFactor();
    float areaStdFactor = options.getAreaSdFactor();
    float subgraphScalpelStdFactor = options.getSubgraphScalpelSdFactor();
    int clusterDistThreshPercentile = options.getClusterDistThreshPercentile();
    if (qStdFactor >= 0) {
      parameters.setStdQFactor(qStdFactor);
    }
    if (areaStdFactor >= 0) {
      parameters.setStdAreaFactor(areaStdFactor);
    }
    if (subgraphScalpelStdFactor >= 0) {
      parameters.setStdSubgraphDistanceFactor(subgraphScalpelStdFactor);
    }
    if (clusterDistThreshPercentile >= 0) {
      parameters.setDistClusterThreshPercentile(clusterDistThreshPercentile);
    }
    if (options.isSubGraphsWonderCutEnabled()) {
      parameters.setCutNonMinimalVerticesEnabled(true);
    } else if (options.isSubGraphsScalpelCutEnabled()) {
      parameters.setCutSubgraphLongestVerticesEnabled(true);
    }
    if (options.isClustersScalpelCutEnabled()) {
      parameters.setCutClusterLongestVerticesEnabled(true);
    }
  }
}
