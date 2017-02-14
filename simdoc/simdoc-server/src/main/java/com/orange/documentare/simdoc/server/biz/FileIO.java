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
import com.orange.documentare.core.prepdata.Metadata;
import com.orange.documentare.core.system.inputfilesconverter.FilesMap;
import com.orange.documentare.simdoc.server.biz.clustering.ClusteringRequest;
import com.orange.documentare.simdoc.server.biz.clustering.ClusteringRequestResult;
import com.orange.documentare.simdoc.server.biz.clustering.RequestValidation;
import lombok.EqualsAndHashCode;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@EqualsAndHashCode
public class FileIO {
  private static final String SAFE_WORKING_DIR = "/safe-working-dir";
  private static final String CLUSTERING_REQUEST_FILE = "/clustering-request.json.gz";
  private static final String CLUSTERING_RESULT_FILE = "/clustering-result.json.gz";
  private static final String CLUSTERING_GRAPH_FILE = "/clustering-graph.json.gz";
  private static final String METADATA_JSON = "metadata.json";

  public final String inputDirectoryAbsPath;
  public final String outputDirectoryAbsPath;

  public FileIO(SharedDirectory sharedDirectory, ClusteringRequest req) {
    String prefix = sharedDirectory.sharedDirectoryAvailable() ?
      sharedDirectory.sharedDirectoryRootPath() :
      "";
    String inPrefixedPath = prefix + req.inputDirectory;
    String outPrefixedPath = prefix + req.outputDirectory;
    inputDirectoryAbsPath = new File(inPrefixedPath).getAbsolutePath();
    outputDirectoryAbsPath = new File(outPrefixedPath).getAbsolutePath();
  }

  public RequestValidation validate() {
    boolean valid = false;
    String error = null;
     if (!inputDirectory().exists()) {
      error = "inputDirectory can not be reached: " + inputDirectoryAbsPath;
    } else if (!inputDirectory().isDirectory()) {
      error = "inputDirectory is not a directory: " + inputDirectoryAbsPath;
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
    cleanupClustering();
    FileUtils.deleteQuietly(clusteringRequestFile());
    FileUtils.deleteQuietly(clusteringResultFile());
    FileUtils.deleteQuietly(clusteringGraphFile());
  }

  public void cleanupClustering() {
    FileUtils.deleteQuietly(safeWorkingDirectory());
    FileUtils.deleteQuietly(metadataFile());
  }

  public FilesMap loadFilesMap() {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
    try {
      return ((Metadata)jsonGenericHandler.getObjectFromJsonFile(Metadata.class, metadataFile()))
        .filesMap;
    } catch (IOException e) {
      throw new IllegalStateException(String.format("Failed to load metadata json '%s': %s", metadataFile().getAbsolutePath(), e.getMessage()));
    }
  }

  public File safeWorkingDirectory() {
    return new File(outputDirectoryAbsPath + SAFE_WORKING_DIR);
  }

  public File inputDirectory() {
    return new File(inputDirectoryAbsPath);
  }

  private File outputDirectory() {
    return new File(outputDirectoryAbsPath);
  }

  private void writeOnDisk(Object o, File file) throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    jsonGenericHandler.getMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    jsonGenericHandler.writeObjectToJsonGzipFile(o, file);
  }

  public File metadataFile() {
    return new File(outputDirectoryAbsPath + "/" + METADATA_JSON);
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
