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
import com.orange.documentare.app.graph.importexport.VertexAttributeProvider;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.JGraphEdge;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.JGraphTBuilder;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.common.CommandLineException;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphEdge;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import org.apache.commons.cli.*;
import org.jgrapht.Graph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.graph.AbstractBaseGraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GraphWriter {
  private static final String DOT_OUTPUT = "graph.dot";
  private static CommandLineOptions options;

  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
try {
      options = new CommandLineOptions(args);
    } catch (CommandLineException e) {
      CommandLineOptions.showHelp();
      return;
    }
    try {
      doTheJob(options);
    } catch (CommandLineException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void doTheJob(CommandLineOptions options) throws IOException {
    ClusteringGraph clusteringGraph = getClusteringGraphFrom(options.getGraphJsonFile());
    AbstractBaseGraph<GraphItem, JGraphEdge> graph = getJGraphTGraph(clusteringGraph);
    export(graph, DOT_OUTPUT);
  }

  private static ClusteringGraph getClusteringGraphFrom(File inputJsonFile) throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
    return (ClusteringGraph) jsonGenericHandler.getObjectFromJsonGzipFile(ClusteringGraph.class, inputJsonFile);
  }

  private static AbstractBaseGraph<GraphItem, JGraphEdge> getJGraphTGraph(ClusteringGraph clusteringGraph) {
    JGraphTBuilder jGraphTBuilder = new JGraphTBuilder();
    return jGraphTBuilder.getJGraphTFrom(clusteringGraph);
  }

  private static void export(AbstractBaseGraph<GraphItem, JGraphEdge> graph, String fileName) throws IOException {
    DOTExporter exporter = new DOTExporter(new IdProvider(), null, new EdgeLabelProvider(), new VertexAttributeProvider(options), null);
    FileWriter writer = new FileWriter(fileName);
    exporter.export(writer, graph);
  }
}
