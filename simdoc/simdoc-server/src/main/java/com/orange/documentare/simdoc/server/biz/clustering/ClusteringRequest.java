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
import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ClusteringRequest {
  @ApiModelProperty(example = "myDocuments")
  public final String inputDirectory;
  @ApiModelProperty(example = "{[{'id':'titi', 'filepath':'/home/titi', 'bytes':[0x1,0x3...]},...])")
  public final BytesData[] bytesData;
  public final boolean bytesDataMode;
  @ApiModelProperty(example = "clusteringOutput", required = true)
  public final String outputDirectory;
  @ApiModelProperty(example = "true")
  public final Boolean debug;
  @ApiModelProperty(example = "2")
  public final Float acutSdFactor;
  @ApiModelProperty(example = "2")
  public final Float qcutSdFactor;
  @ApiModelProperty(example = "2")
  public final Float scutSdFactor;
  @ApiModelProperty(example = "75")
  public final Integer ccutPercentile;
  @ApiModelProperty(example = "false")
  public final Boolean wcut;
  @ApiModelProperty(example = "5")
  public final Integer kNearestNeighboursThreshold;

  public RequestValidation validate() {
    boolean valid = false;
    String error = null;
    if (inputDirectory == null && bytesData == null) {
      error = "inputDirectory and bytesData are missing";
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
    private BytesData[] bytesData;
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
      boolean bytesDataMode = bytesData != null;
      return new ClusteringRequest(inputDirectory, bytesData, bytesDataMode, outputDirectory, debug, acutSdFactor, qcutSdFactor, scutSdFactor, ccutPercentile, wcut, kNearestNeighboursThreshold);
    }


    public ClusteringRequestBuilder inputDirectory(String inputDirectory) {
      this.inputDirectory = inputDirectory;
      return this;
    }

    public ClusteringRequestBuilder bytesData(BytesData[] bytesData) {
      this.bytesData = bytesData;
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
