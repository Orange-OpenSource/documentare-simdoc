package com.orange.documentare.app.graph;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.graph.cmdline.CommandLineOptions;
import com.orange.documentare.app.graph.importexport.EdgeLabelProvider;
import com.orange.documentare.app.graph.importexport.IdProvider;
import com.orange.documentare.app.graph.importexport.LabelProvider;
import com.orange.documentare.app.graph.importexport.VertexAttributeProvider;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.JGraphEdge;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.JGraphTBuilder;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import com.orange.documentare.core.prepdata.Metadata;
import com.orange.documentare.core.system.inputfilesconverter.FilesMap;
import org.apache.commons.cli.ParseException;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.AbstractBaseGraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class GraphWriter {
  private static final String DOT_OUTPUT = "graph.dot";
  private static CommandLineOptions options;

  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
    System.out.println("\n[Graph - Start]");
    try {
      options = new CommandLineOptions(args);
    } catch (Exception e) {
      CommandLineOptions.showHelp();
      return;
    }
    try {
      doTheJob(options);
      System.out.println("\n[Graph - Done]");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void doTheJob(CommandLineOptions options) throws IOException {
    Optional<String> imageDirectoryAbsPath = buildThumbnails();
    ClusteringGraph clusteringGraph = getClusteringGraphFrom(options.getGraphJsonFile());
    AbstractBaseGraph<GraphItem, JGraphEdge> graph = getJGraphTGraph(clusteringGraph);
    export(graph, DOT_OUTPUT, imageDirectoryAbsPath);
  }

  private static Optional<String> buildThumbnails() throws IOException {
    return options.hasImageDirectory() ?
      Optional.of(options.getImageDirectory()) : options.hasMetadata() ? doBuildThumbnails(options.getMetadata()) : Optional.empty();
  }

  private static ClusteringGraph getClusteringGraphFrom(File inputJsonFile) throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
    return (ClusteringGraph) jsonGenericHandler.getObjectFromJsonGzipFile(ClusteringGraph.class, inputJsonFile);
  }

  private static AbstractBaseGraph<GraphItem, JGraphEdge> getJGraphTGraph(ClusteringGraph clusteringGraph) {
    JGraphTBuilder jGraphTBuilder = new JGraphTBuilder();
    return jGraphTBuilder.getJGraphTFrom(clusteringGraph);
  }

  private static void export(AbstractBaseGraph<GraphItem, JGraphEdge> graph, String fileName, Optional<String> imageDirectoryAbsPath) throws IOException {
    DOTExporter exporter = new DOTExporter(new IdProvider(), labelProvider(), new EdgeLabelProvider(), new VertexAttributeProvider(imageDirectoryAbsPath), null);
    FileWriter writer = new FileWriter(fileName);
    exporter.export(writer, graph);
  }

  private static VertexNameProvider labelProvider() throws IOException {
    if (options.hasMetadata()) {
      JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
      FilesMap filesMap = ((Metadata)jsonGenericHandler.getObjectFromJsonFile(Metadata.class, options.getMetadata())).filesMap;
      return new LabelProvider(filesMap);
    } else {
      return null;
    }
  }

  private static Optional<String> doBuildThumbnails(File metadataFile) throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
    Metadata metadata = (Metadata) jsonGenericHandler.getObjectFromJsonFile(Metadata.class, metadataFile);
    ThumbnailsBuilder thumbnailsBuilder = new ThumbnailsBuilder(metadata);
    return thumbnailsBuilder.build();
  }
}
