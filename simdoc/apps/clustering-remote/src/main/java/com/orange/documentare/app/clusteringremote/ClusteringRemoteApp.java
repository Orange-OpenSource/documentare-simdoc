package com.orange.documentare.app.clusteringremote;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.clusteringremote.cmdline.ClusteringRemoteOptions;
import com.orange.documentare.app.clusteringremote.cmdline.CommandLineOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;

@Slf4j
public class ClusteringRemoteApp {
  private static final File SIMDOC_EXPORT_FILE = new File("sc_segmentation_ready_for_user_interface.json.gz");
  private static final File GRAPH_OUTPUT = new File("sc_graph_input.json.gz");

  private static ClusteringRemoteOptions options;

  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
    System.out.println("\n[ClusteringRemote - Start]");
    try {
      options = (new CommandLineOptions(args)).simClusteringOptions();
    } catch (Exception e) {
      CommandLineOptions.showHelp(e);
      return;
    }
    try {
      doTheJob();
      System.out.println("\n[ClusteringRemote - End]");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void doTheJob() throws IOException {
    System.out.println("Clustering parameters, " + options.clusteringParameters.toString());
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(options.inputDirectory)
      .outputDirectory(options.outputDirectory)
      .acut(options.clusteringParameters.acutSdFactor)
      .qcut(options.clusteringParameters.qcutSdFactor)
      .scut(options.clusteringParameters.scutSdFactor)
      .ccut(options.clusteringParameters.ccutPercentile)
      .wcut()
      .k(options.clusteringParameters.kNearestNeighboursThreshold)
      .sloop()
      .build();

    RemoteClustering remoteClustering = new RemoteClustering();
    remoteClustering.request("http://localhost:8080", req);

  /*  if (options.simdoc) {
      doTheJobForSimDoc();
    } else {
      doTheJobForRegularFiles();
    }*/
  }

/*  private static void doTheJobForSimDoc() throws IOException {
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
    return clusteringGraphBuilder.buildGraphAndUpdateClusterIdAndCenter(items, options.clusteringParameters);
  }*/
}
