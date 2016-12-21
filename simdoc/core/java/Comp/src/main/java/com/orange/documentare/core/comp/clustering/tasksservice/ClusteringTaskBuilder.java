package com.orange.documentare.core.comp.clustering.tasksservice;

import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;

public class ClusteringTaskBuilder {
  private String inputFilename;
  private String outputFilename;
  private ClusteringParameters clusteringParameters;

  private boolean clearBytes = true;
  private String outputGraphFilename;
  private String strippedOutputFilename;
  private GraphWriter graphWriter;
  private boolean prettyPrint = true;

  ClusteringTaskBuilder() {
  }

  public ClusteringTask build() {
    return new ClusteringTask(inputFilename, outputFilename, clusteringParameters, clearBytes, outputGraphFilename, strippedOutputFilename, graphWriter, prettyPrint);
  }

  public ClusteringTaskBuilder inputFilename(String inputFilename) {
    this.inputFilename = inputFilename;
    return this;
  }

  public ClusteringTaskBuilder outputFilename(String outputFilename) {
    this.outputFilename = outputFilename;
    return this;
  }

  public ClusteringTaskBuilder clusteringParameters(ClusteringParameters clusteringParameters) {
    this.clusteringParameters = clusteringParameters;
    return this;
  }

  public ClusteringTaskBuilder clearBytes(boolean clearBytes) {
    this.clearBytes = clearBytes;
    return this;
  }

  public ClusteringTaskBuilder strippedOutputFilename(String strippedOutputFilename) {
    this.strippedOutputFilename = strippedOutputFilename;
    return this;
  }

  public ClusteringTaskBuilder graphWriter(GraphWriter graphWriter) {
    this.graphWriter = graphWriter;
    return this;
  }

  public ClusteringTaskBuilder prettyPrint(boolean prettyPrint) {
    this.prettyPrint = prettyPrint;
    return this;
  }

  public ClusteringTaskBuilder writeGraphTo(String outputGraphFilename) {
    clearBytes = false;
    this.outputGraphFilename = outputGraphFilename;
    return this;
  }
}
