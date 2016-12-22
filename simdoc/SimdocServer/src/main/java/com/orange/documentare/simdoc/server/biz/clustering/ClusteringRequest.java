package com.orange.documentare.simdoc.server.biz.clustering;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.File;

@ToString
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class ClusteringRequest {
  public final String inputDirectory;
  public final String outputDirectory;
  public final Parameters parameters;

  public RequestValidation validate() {
    boolean valid = false;
    String error = null;
    if (inputDirectory == null) {
      error = "inputDirectory is missing";
    } else if (!(new File(inputDirectory).exists())) {
      error = "inputDirectory can not be reached: " + inputDirectory;
    } else if (!(new File(inputDirectory).isDirectory())) {
      error = "inputDirectory is not a directory: " + inputDirectory;
    } else if (outputDirectory == null) {
      error = "outputDirectory is missing";
    } else if (!(new File(outputDirectory).exists())) {
      error = "outputDirectory can not be reached: " + outputDirectory;
    } else if (!(new File(outputDirectory).isDirectory())) {
      error = "outputDirectory is not a directory: " + outputDirectory;
    } else if (!(new File(outputDirectory).canWrite())) {
      error = "outputDirectory is not writable: " + outputDirectory;
    } else {
      valid = true;
    }
    return new RequestValidation(valid, error);
  }

  public static ClusteringRequestBuilder builder() {
    return new ClusteringRequestBuilder();
  }

  @RequiredArgsConstructor
  public static class RequestValidation {
    public final boolean ok;
    public final String error;
  }

  public static class ClusteringRequestBuilder {
    private String inputDirectory;
    private String outputDirectory;
    private Parameters parameters = Parameters.defaultParameters();

    private ClusteringRequestBuilder() {
    }

    public ClusteringRequest build() {
      return new ClusteringRequest(inputDirectory, outputDirectory, parameters);
    }


    public ClusteringRequestBuilder inputDirectory(String inputDirectory) {
      this.inputDirectory = inputDirectory;
      return this;
    }

    public ClusteringRequestBuilder outputDirectory(String outputDirectory) {
      this.outputDirectory = outputDirectory;
      return this;
    }

    public ClusteringRequestBuilder parameters(Parameters parameters) {
      this.parameters = parameters;
      return this;
    }
  }

  @ToString
  @RequiredArgsConstructor
  public static class Parameters {
    public final boolean debug;
    public final float qcutSdFactor;
    public final float acutSdFactor;
    public final float scutSdFactor;
    public final int ccutPercentile;
    public final boolean wcut;
    public final int kNearestNeighboursThreshold;

    public static Parameters defaultParameters() {
      ClusteringParameters def = ClusteringParameters.builder().build();
      return new Parameters(false, def.qcutSdFactor, def.acutSdFactor, def.scutSdFactor, def.ccutPercentile, def.wcut, def.kNearestNeighboursThreshold);
    }
  }
}
