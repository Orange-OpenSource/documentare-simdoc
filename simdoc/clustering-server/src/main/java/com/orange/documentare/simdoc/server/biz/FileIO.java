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
import com.orange.documentare.simdoc.server.biz.clustering.ClusteringRequest;
import com.orange.documentare.simdoc.server.biz.clustering.ClusteringRequestResult;
import com.orange.documentare.simdoc.server.biz.clustering.RequestValidation;
import lombok.EqualsAndHashCode;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@EqualsAndHashCode
public class FileIO {
  private static final String CLUSTERING_REQUEST_FILE = "/clustering-request.json.gz";
  private static final String CLUSTERING_RESULT_FILE = "/clustering-result.json.gz";
  private static final String CLUSTERING_GRAPH_FILE = "/clustering-graph.json.gz";

  public final String outputDirectoryAbsPath;
  private final boolean bytesDataMode;

  public FileIO(SharedDirectory sharedDirectory, ClusteringRequest req) {
    this.bytesDataMode = req.bytesDataMode();


    String prefix = sharedDirectory.sharedDirectoryAvailable() ?
      sharedDirectory.sharedDirectoryRootPath() :
      "";

    outputDirectoryAbsPath = req.outputDirectory == null ? null : new File(prefix + req.outputDirectory).getAbsolutePath();
  }

  public RequestValidation validate() {
    boolean valid = false;
    String error = null;

     if (!bytesDataMode) {
      error = "bytesDataMode is necessary";
    } else if (!outputDirectory().exists()) {
      error = "outputDirectory can not be reached: " + outputDirectoryAbsPath;
    } else if (!outputDirectory().isDirectory()) {
      error = "outputDirectory is not a directory: " + outputDirectoryAbsPath;
    } else if (!outputDirectory().canWrite()) {
      error = "outputDirectory is not writable: " + outputDirectoryAbsPath;
    } else {
      valid = true;
    }
    return new RequestValidation(valid, error);
  }

  public void writeClusteringRequestResult(ClusteringRequestResult clusteringRequestResult) throws IOException {
    writeOnDisk(clusteringRequestResult, clusteringResultFile());
  }

  public void writeClusteringGraph(ClusteringGraph graph) throws IOException {
    writeOnDisk(graph, clusteringGraphFile());
  }

  public void writeRequest(ClusteringRequest req) throws IOException {
    writeOnDisk(req, clusteringRequestFile());
  }

  public void deleteAllClusteringFiles() {
    FileUtils.deleteQuietly(clusteringRequestFile());
    FileUtils.deleteQuietly(clusteringResultFile());
    FileUtils.deleteQuietly(clusteringGraphFile());
  }


  private File outputDirectory() {
    return new File(outputDirectoryAbsPath);
  }

  private void writeOnDisk(Object o, File file) throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    jsonGenericHandler.getMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    jsonGenericHandler.writeObjectToJsonGzipFile(o, file);
  }


  private File clusteringRequestFile() {
    return new File(outputDirectoryAbsPath + CLUSTERING_REQUEST_FILE);
  }

  private File clusteringGraphFile() {
    return new File(outputDirectoryAbsPath + CLUSTERING_GRAPH_FILE);
  }

  private File clusteringResultFile() {
    return new File(outputDirectoryAbsPath + CLUSTERING_RESULT_FILE);
  }

}
