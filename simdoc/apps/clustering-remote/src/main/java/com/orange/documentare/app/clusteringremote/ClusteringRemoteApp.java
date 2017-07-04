package com.orange.documentare.app.clusteringremote;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.clusteringremote.cmdline.ClusteringRemoteOptions;
import com.orange.documentare.app.clusteringremote.cmdline.CommandLineOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;

@Slf4j
public class ClusteringRemoteApp {

  private static ClusteringRemoteOptions options;

  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
    System.out.println("\n[ClusteringRemote - Start]");
    try {
      options = (new CommandLineOptions(args)).simClusteringOptions();
    } catch (Exception e) {
      CommandLineOptions.showHelp(e);
      return;
    }
    try {
      doTheJob();
      System.out.println("\n[ClusteringRemote - End]");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void doTheJob() throws IOException {
    System.out.println("Clustering parameters, " + options.clusteringParameters.toString());
    ClusteringRequest req;
    if (options.debug) {
      req = ClusteringRequest.builder()
        .inputDirectory(options.inputDirectory)
        .outputDirectory(options.outputDirectory)
        .debug()
        .acut(options.clusteringParameters.acutSdFactor)
        .qcut(options.clusteringParameters.qcutSdFactor)
        .scut(options.clusteringParameters.scutSdFactor)
        .ccut(options.clusteringParameters.ccutPercentile)
        .wcut()
        .k(options.clusteringParameters.kNearestNeighboursThreshold)
        .sloop()
        .build();

    } else {
      req = ClusteringRequest.builder()
        .inputDirectory(options.inputDirectory)
        .outputDirectory(options.outputDirectory)
        .acut(options.clusteringParameters.acutSdFactor)
        .qcut(options.clusteringParameters.qcutSdFactor)
        .scut(options.clusteringParameters.scutSdFactor)
        .ccut(options.clusteringParameters.ccutPercentile)
        .wcut()
        .k(options.clusteringParameters.kNearestNeighboursThreshold)
        .sloop()
        .build();

    }
    RemoteClustering remoteClustering = new RemoteClustering();
    remoteClustering.request("http://localhost:8080", req);
  }
}
