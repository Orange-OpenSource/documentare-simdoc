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

@Slf4j
public class ClusteringRemoteApp {

  private static ClusteringRequest clusteringRequest;

  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
    System.out.println("\n[ClusteringRemote - Start]");
    try {
      clusteringRequest = (new CommandLineOptions(args)).clusteringRequest();
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
    System.out.println("Clustering parameters, " + clusteringRequest);
    RemoteClustering remoteClustering = new RemoteClustering();
    remoteClustering.request("http://localhost:8080", clusteringRequest);
  }
}
