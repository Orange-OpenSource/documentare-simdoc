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
import com.orange.documentare.app.simclustering.cmdline.SimClusteringOptions;
import com.orange.documentare.core.comp.clustering.graph.ClusteringGraphBuilder;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;

@Slf4j
public class SimClustering {
  private static final File SIMDOC_EXPORT_FILE = new File("sc_segmentation_ready_for_user_interface.json.gz");
  private static final File GRAPH_OUTPUT = new File("sc_graph_input.json.gz");

  private static SimClusteringOptions options;

  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
    try {
      options = (new CommandLineOptions(args)).simClusteringOptions();
    } catch (Exception e) {
      CommandLineOptions.showHelp(e);
      return;
    }
    try {
      doTheJob();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void doTheJob() throws IOException {
    System.out.println("Clustering parameters, " + options.clusteringParameters.toString());
    if (options.simdoc) {
      doTheJobForSimDoc();
    } else {
      doTheJobForRegularFiles();
    }
  }

  private static void doTheJobForSimDoc() throws IOException {
    ImageSegmentation imageSegmentation = segmentationOf();
    DigitalTypes copyWithoutSpaces = imageSegmentation.getDigitalTypes().copyWithoutSpaces();
    ClusteringItem[] items = copyWithoutSpaces.toArray(new DigitalType[copyWithoutSpaces.size()]);
    computeClustering(items);
    SimDocExport export = new SimDocExport(imageSegmentation);
    export.exportTo(SIMDOC_EXPORT_FILE);
  }

  private static ImageSegmentation segmentationOf() throws IOException {
    JsonGenericHandler jsonHandler = new JsonGenericHandler(true);
    return (ImageSegmentation) jsonHandler.getObjectFromJsonGzipFile(ImageSegmentation.class, options.simdocFile);
  }

  private static void doTheJobForRegularFiles() throws IOException {
    InputItem[] inputItems = getInputItemsFrom(options.regularFile);
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
    return clusteringGraphBuilder.build(items, options.clusteringParameters);
  }
}
