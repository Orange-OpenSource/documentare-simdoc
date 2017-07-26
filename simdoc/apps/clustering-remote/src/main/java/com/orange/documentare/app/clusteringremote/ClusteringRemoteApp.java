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

import com.orange.documentare.app.clusteringremote.cmdline.CommandLineOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.Currency;

@Slf4j
public class ClusteringRemoteApp {

  private static ClusteringRequest remoteClusteringRequest;

  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
    System.out.println("\n[ClusteringRemote - Start]");
    try {
      remoteClusteringRequest = (new CommandLineOptions(args)).clusteringRequest();
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
    System.out.println("Clustering parameters, " + remoteClusteringRequest);
    RemoteClustering remoteClustering = new RemoteClustering();
    String url = remoteClusteringRequest.url;
    ClusteringRequest clusteringRequest = buildClusteringRequest();

    remoteClustering.request(url, clusteringRequest);
  }

  private static ClusteringRequest buildClusteringRequest() {
    ClusteringRequest.ClusteringRequestBuilder clusteringRequestBuilder = ClusteringRequest.builder()
      .bytesData(remoteClusteringRequest.bytesData)
      .outputDirectory(remoteClusteringRequest.outputDirectory);

    if (remoteClusteringRequest.acutSdFactor != null) {
      clusteringRequestBuilder.acut(remoteClusteringRequest.acutSdFactor);
    }

    if (remoteClusteringRequest.ccutPercentile != null) {
      clusteringRequestBuilder.ccut(remoteClusteringRequest.ccutPercentile);
    }

    if (remoteClusteringRequest.qcutSdFactor != null) {
      clusteringRequestBuilder.qcut(remoteClusteringRequest.qcutSdFactor);
    }

    if (remoteClusteringRequest.scutSdFactor != null) {
      clusteringRequestBuilder.scut(remoteClusteringRequest.scutSdFactor);
    }


    if ((remoteClusteringRequest.wcut != null) && (remoteClusteringRequest.wcut == true)) {
      clusteringRequestBuilder.wcut();
    }

    if ((remoteClusteringRequest.debug != null) && (remoteClusteringRequest.debug == true)) {
      clusteringRequestBuilder.debug();
    }

    if ((remoteClusteringRequest.sloop != null) && (remoteClusteringRequest.sloop == true)) {
      clusteringRequestBuilder.sloop();
    }

    return clusteringRequestBuilder
      .build();
  }
}
