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
import com.orange.documentare.core.model.common.CommandLineException;

import java.io.File;

public class SimClusteringOptions {
  public final boolean simdoc;
  public final File regularFile;
  public final File simdocFile;
  public final ClusteringParameters clusteringParameters;

  private SimClusteringOptions(File regularFile, File simdocFile, ClusteringParameters clusteringParameters) {
    this.regularFile = regularFile;
    this.simdocFile = simdocFile;
    this.clusteringParameters = clusteringParameters;
    simdoc = simdocFile != null;
  }

  public static SimClusteringOptionsBuilder builder() {
    return new SimClusteringOptionsBuilder();
  }

  public static class SimClusteringOptionsBuilder {
    public final ClusteringParameters.ClusteringParametersBuilder clusteringParametersBuilder = ClusteringParameters.builder();
    private File regularFile;
    private File simdocFile;

    private SimClusteringOptionsBuilder() {

    }

    public void regularFile(String optionValue) {
      regularFile = new File(optionValue);
    }

    public void simdocFile(String optionValue) {
      simdocFile = new File(optionValue);
    }

    public SimClusteringOptions build() {
      checkOptions();
      return new SimClusteringOptions(regularFile, simdocFile, clusteringParametersBuilder.build());
    }

    private void checkOptions() {
      if ((regularFile == null || !regularFile.exists()) && (simdocFile == null || !simdocFile.exists())) {
        throw new CommandLineException("Missing input file");
      }
    }
  }
}
