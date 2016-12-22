package com.orange.documentare.simdoc.server.biz.clustering;

import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.File;

@ToString
@RequiredArgsConstructor
public class ClusteringRequest {
  public final String inputDirectory;
  public final String outputDirectory;
  public final Boolean debug;
  public final Float acutSdFactor;
  public final Float qcutSdFactor;
  public final Float scutSdFactor;
  public final Integer ccutPercentile;
  public final Boolean wcut;
  public final Integer kNearestNeighboursThreshold;

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

  public ClusteringParameters clusteringParameters() {
    return null;
  }

  public boolean debug() {
    return debug != null && debug;
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
    private Boolean debug;
    private Float acutSdFactor;
    private Float qcutSdFactor;
    private Float scutSdFactor;
    private Integer ccutPercentile;
    private Boolean wcut;
    private Integer kNearestNeighboursThreshold;

    private ClusteringRequestBuilder() {
    }

    public ClusteringRequest build() {
      return new ClusteringRequest(inputDirectory, outputDirectory, debug, acutSdFactor, qcutSdFactor, scutSdFactor, ccutPercentile, wcut, kNearestNeighboursThreshold);
    }


    public ClusteringRequestBuilder inputDirectory(String inputDirectory) {
      this.inputDirectory = inputDirectory;
      return this;
    }

    public ClusteringRequestBuilder outputDirectory(String outputDirectory) {
      this.outputDirectory = outputDirectory;
      return this;
    }

    public ClusteringRequestBuilder debug() {
      debug = true;
      return this;
    }

    public ClusteringRequestBuilder acut(float acutSdFactor) {
      this.acutSdFactor = acutSdFactor;
      return this;
    }

    public ClusteringRequestBuilder qcut(float qcutSdFactor) {
      this.qcutSdFactor = qcutSdFactor;
      return this;
    }

    public ClusteringRequestBuilder scut(float scutSdFactor) {
      this.scutSdFactor = scutSdFactor;
      return this;
    }

    public ClusteringRequestBuilder ccut(int ccutPercentile) {
      this.ccutPercentile = ccutPercentile;
      return this;
    }

    public ClusteringRequestBuilder wcut() {
      wcut = true;
      return this;
    }

    public ClusteringRequestBuilder k(int kNearestNeighboursThreshold) {
      this.kNearestNeighboursThreshold = kNearestNeighboursThreshold;
      return this;
    }
  }
}
