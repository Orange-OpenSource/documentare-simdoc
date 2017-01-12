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

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class ClusteringParameters {
  public static final float A_DEFAULT_SD_FACTOR = 2;
  public static final float Q_DEFAULT_SD_FACTOR = 2;
  public static final float SCUT_DEFAULT_SD_FACTOR = 2;
  public static final int CCUT_DEFAULT_PERCENTILE = 75;

  public final float qcutSdFactor;
  public final float acutSdFactor;
  public final float scutSdFactor;
  public final int ccutPercentile;
  public final boolean wcut;
  public final int kNearestNeighboursThreshold;

  private ClusteringParameters(float qcutSdFactor, float acutSdFactor, float scutSdFactor, int ccutPercentile, boolean wcut, int kNearestNeighboursThreshold) {
    this.qcutSdFactor = qcutSdFactor;
    this.acutSdFactor = acutSdFactor;
    this.scutSdFactor = scutSdFactor;
    this.ccutPercentile = ccutPercentile;
    this.wcut = wcut;
    this.kNearestNeighboursThreshold = kNearestNeighboursThreshold;
  }

  public static ClusteringParametersBuilder builder() {
    return new ClusteringParametersBuilder();
  }

  public boolean acut() {
    return acutSdFactor > 0;
  }
  public boolean qcut() {
    return qcutSdFactor > 0;
  }
  public boolean scut() {
    return scutSdFactor > 0;
  }
  public boolean ccut() {
    return ccutPercentile > 0;
  }
  public boolean knn() {
    return kNearestNeighboursThreshold > 0;
  }


  @Override
  public String toString() {
    String str = String.format("acut=" + acut() + ", qcut=" + qcut() + ", scut=" + scut() + ", ccut=" + ccut() + ", wcut=" + wcut + ", knn=" + knn());
    str += acut() ? ", acutSd=" + acutSdFactor : "";
    str += qcut() ? ", qcutSd=" + qcutSdFactor : "";
    str += scut() ? ", scutSd=" + scutSdFactor : "";
    str += ccut() ? ", ccutSd=" + ccutPercentile : "";
    str += knn() ? ", knn=" + kNearestNeighboursThreshold : "";
    return str;
  }

  public static class ClusteringParametersBuilder {
    private float qcutSdFactor = -1;
    private float acutSdFactor = -1;
    private float scutSdFactor = -1;
    private int ccutPercentile = -1;
    private boolean wcut;
    private int kNearestNeighboursThreshold = -1;

    private ClusteringParametersBuilder() {
    }

    public ClusteringParameters build() {
      return new ClusteringParameters(qcutSdFactor, acutSdFactor, scutSdFactor, ccutPercentile, wcut, kNearestNeighboursThreshold);
    }

    public ClusteringParametersBuilder qcut() {
      return qcut(Q_DEFAULT_SD_FACTOR);
    }
    public ClusteringParametersBuilder qcut(float qcutSdFactor) {
      this.qcutSdFactor = qcutSdFactor;
      return this;
    }

    public ClusteringParametersBuilder acut() {
      return acut(A_DEFAULT_SD_FACTOR);
    }
    public ClusteringParametersBuilder acut(float acutSdFactor) {
      this.acutSdFactor = acutSdFactor;
      return this;
    }

    public ClusteringParametersBuilder scut() {
      return scut(SCUT_DEFAULT_SD_FACTOR);
    }
    public ClusteringParametersBuilder scut(float scutSdFactor) {
      this.scutSdFactor = scutSdFactor;
      return this;
    }

    public ClusteringParametersBuilder ccut() {
      return ccut(CCUT_DEFAULT_PERCENTILE);
    }
    public ClusteringParametersBuilder ccut(int ccutPercentile) {
      this.ccutPercentile = ccutPercentile;
      return this;
    }

    public ClusteringParametersBuilder wcut() {
      wcut = true;
      return this;
    }

    public ClusteringParametersBuilder knn(int kNearestNeighboursThreshold) {
      this.kNearestNeighboursThreshold = kNearestNeighboursThreshold;
      return this;
    }
  }
}
