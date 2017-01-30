package com.orange.documentare.core.comp.clustering.graph;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.io.Gzip;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ClusteringGraphBuilderIntegrationScalpelTest {
  private static final String CLUSTERING_INPUT = "/bestioles_nearests_for_clustering.json.gz";
  private static final String GRAPH_OUTPUT_REF = "/bestioles_graph_scalpel_ref.json.gz";
  private static final String GRAPH_OUTPUT = "bestioles_graph_scalpel.json.gz";

  @After
  public void cleanup() {
    FileUtils.deleteQuietly(new File(GRAPH_OUTPUT));
  }

  @Test
  public void shouldBuildGraphFromClusteringItemsInput() throws IOException {
    // given
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    ImportModel importModel = (ImportModel) jsonGenericHandler.getObjectFromJsonGzipFile(ImportModel.class, new File(getClass().getResource(CLUSTERING_INPUT).getFile()));
    importModel.loadItemsBytes();
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringParameters clusteringParameters = ClusteringParameters.builder()
            .acut()
            .qcut()
            .scut(1)
            .build();
    // do
    ClusteringGraph clusteringGraph = clusteringGraphBuilder.build(importModel.getItems(), clusteringParameters);
    File output = new File(GRAPH_OUTPUT);
    jsonGenericHandler.writeObjectToJsonGzipFile(clusteringGraph, output);
    // then
    File expected = new File(getClass().getResource(GRAPH_OUTPUT_REF).getFile());
    String expectedJsonString = Gzip.getStringFromGzipFile(expected);
    String outputJsonString = Gzip.getStringFromGzipFile(output);
    Assert.assertEquals(expectedJsonString, outputJsonString);
  }
}
