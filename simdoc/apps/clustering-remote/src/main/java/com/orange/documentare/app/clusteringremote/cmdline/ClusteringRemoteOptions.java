package com.orange.documentare.app.clusteringremote.cmdline;
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
import com.orange.documentare.core.system.CommandLineException;

import java.io.File;

public class ClusteringRemoteOptions {
  public final String inputDirectory;
  public final String outputDirectory;
  public Boolean debug = false;
  public final ClusteringParameters clusteringParameters;


  private ClusteringRemoteOptions(String inputDirectory, String outputDirectory, Boolean debug, ClusteringParameters clusteringParameters) {
    this.outputDirectory = outputDirectory;
    this.inputDirectory = inputDirectory;
    this.debug = debug;
    this.clusteringParameters = clusteringParameters;
  }

  public static SimClusteringOptionsBuilder builder() {
    return new SimClusteringOptionsBuilder();
  }

  public static class SimClusteringOptionsBuilder {
    public final ClusteringParameters.ClusteringParametersBuilder clusteringParametersBuilder = ClusteringParameters.builder();
    private String inputDirectory;
    private String outputDirectory;
    private Boolean debug = false;

    private SimClusteringOptionsBuilder() {

    }

    public void inputDirectory(String optionValue) {
      inputDirectory = optionValue;
    }

    public void outputDirectory(String optionValue) {
      outputDirectory = optionValue;
    }


    public ClusteringRemoteOptions build() {
      checkOptions();
      return new ClusteringRemoteOptions(inputDirectory, outputDirectory, debug, clusteringParametersBuilder.build());
    }

    private void checkOptions() {
      if ((inputDirectory == null ) && (outputDirectory == null )) {
        throw new CommandLineException("Missing input file");
      }
    }

    public void debug() {
      debug = true;
    }
  }
}
