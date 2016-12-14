package com.orange.documentare.app.simclustering.cmdline;
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

import java.io.File;

public class SimClusteringOptions {
  public final boolean simdoc;
  public final boolean qcut;
  public final boolean acut;
  public final boolean scut;
  public final boolean ccut;
  public final boolean wcut;
  public final File regularFile;
  public final File simdocFile;
  public final float qcutSdFactor;
  public final float acutSdFactor;
  public final float scutSdFactor;
  public final int ccutPercentile;

  public SimClusteringOptions(File regularFile, File simdocFile, float qcutSdFactor, float acutSdFactor, float scutSdFactor, int ccutPercentile, boolean wcut) {
    this.regularFile = regularFile;
    this.simdocFile = simdocFile;
    this.qcutSdFactor = qcutSdFactor;
    this.acutSdFactor = acutSdFactor;
    this.scutSdFactor = scutSdFactor;
    this.ccutPercentile = ccutPercentile;
    this.wcut = wcut;

    simdoc = simdocFile != null;
    qcut = qcutSdFactor >= 0;
    acut = qcutSdFactor >= 0;
    scut = qcutSdFactor >= 0;
    ccut = qcutSdFactor >= 0;
  }

  public ClusteringParameters clusteringParameters() {
    ClusteringParameters parameters = new ClusteringParameters();

    if (qcut) {
      parameters.setStdQFactor(qcutSdFactor);
    }
    if (acut) {
      parameters.setStdAreaFactor(acutSdFactor);
    }
    if (scut) {
      parameters.setCutSubgraphLongestVerticesEnabled(true);
      parameters.setStdSubgraphDistanceFactor(scutSdFactor);
    }
    if (ccut) {
      parameters.setCutClusterLongestVerticesEnabled(true);
      parameters.setDistClusterThreshPercentile(ccutPercentile);
    }
    if (wcut) {
      parameters.setCutNonMinimalVerticesEnabled(true);
    }

    return parameters;
  }
}
