package com.orange.documentare.simdoc.server.biz.clustering;

/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters.ClusteringParametersBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ClusteringRequest {
  @ApiModelProperty(example = "user1/files", required = true)
  public final String inputDirectory;
  @ApiModelProperty(example = "user1/clustering_tmp", required = true)
  public final String outputDirectory;
  @ApiModelProperty(example = "true")
  public final Boolean debug; // FIXME: not used yet
  @ApiModelProperty(example = "2.1")
  public final Float acutSdFactor;
  @ApiModelProperty(example = "3.1")
  public final Float qcutSdFactor;
  @ApiModelProperty(example = "1.5")
  public final Float scutSdFactor;
  @ApiModelProperty(example = "75")
  public final Integer ccutPercentile;
  @ApiModelProperty(example = "true")
  public final Boolean wcut;
  @ApiModelProperty(example = "10")
  public final Integer kNearestNeighboursThreshold;

  public RequestValidation validate() {
    boolean valid = false;
    String error = null;
    if (inputDirectory == null) {
      error = "inputDirectory is missing";
    } else if (outputDirectory == null) {
      error = "outputDirectory is missing";
    } else {
      valid = true;
    }
    return new RequestValidation(valid, error);
  }

  public ClusteringParameters clusteringParameters() {
    ClusteringParametersBuilder builder = ClusteringParameters.builder();
    if (acutSdFactor != null) {
      builder.acut(acutSdFactor);
    }
    if (qcutSdFactor != null) {
      builder.qcut(qcutSdFactor);
    }
    if (scutSdFactor != null) {
      builder.scut(scutSdFactor);
    }
    if (ccutPercentile != null) {
      builder.ccut(ccutPercentile);
    }
    if (wcut != null && wcut) {
      builder.wcut();
    }
    if (kNearestNeighboursThreshold!= null) {
      builder.knn(kNearestNeighboursThreshold);
    }
    return builder.build();
  }

  public boolean debug() {
    return debug != null && debug;
  }

  public static ClusteringRequestBuilder builder() {
    return new ClusteringRequestBuilder();
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
