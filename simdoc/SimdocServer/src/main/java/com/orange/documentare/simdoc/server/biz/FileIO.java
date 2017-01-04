package com.orange.documentare.simdoc.server.biz;

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
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.system.filesid.FilesIdBuilder;
import com.orange.documentare.core.system.filesid.FilesIdMap;
import com.orange.documentare.simdoc.server.biz.clustering.ClusteringRequestResult;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@EqualsAndHashCode
public class FileIO {
  public static final String SAFE_INPUT_DIR = "/safe-input-dir";
  public static final String CLUSTERING_RESULT_FILE = "/clustering-result.json.gz";
  public static final String CLUSTERING_GRAPH_FILE = "/clustering-graph.json.gz";

  private final File inputDirectory;
  private final File outputDirectory;

  public File safeInputDir() {
    return new File(outputDirectory.getAbsolutePath() + SAFE_INPUT_DIR);
  }

  public void writeClusteringRequestResult(ClusteringRequestResult clusteringRequestResult) throws IOException {
    writeOnDisk(clusteringRequestResult, new File(outputDirectory.getAbsolutePath() + CLUSTERING_RESULT_FILE));
  }

  public void writeClusteringGraph(ClusteringGraph graph) throws IOException {
    writeOnDisk(graph, new File(outputDirectory.getAbsolutePath() + CLUSTERING_GRAPH_FILE));
  }

  public void cleanupClustering() {
    FileUtils.deleteQuietly(safeInputDir());
    FileUtils.deleteQuietly(new File(outputDirectory.getAbsolutePath() + "/" + FilesIdBuilder.MAP_NAME));
  }

  private void writeOnDisk(Object o, File file) throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    jsonGenericHandler.getMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    jsonGenericHandler.writeObjectToJsonGzipFile(o, file);
  }

  public String inPath() {
    return inputDirectory.getAbsolutePath();
  }
  public String outPath() {
    return outputDirectory.getAbsolutePath();
  }
}
