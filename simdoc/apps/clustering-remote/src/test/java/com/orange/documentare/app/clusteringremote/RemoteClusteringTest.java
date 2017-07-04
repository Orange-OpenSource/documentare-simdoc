package com.orange.documentare.app.clusteringremote;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class RemoteClusteringTest {

  private static final String INPUT_DIRECTORY = "/home/obelix/workspace/documentare-simdoc/simdoc/simdoc-server/target/test-classes/animals-dna";
  private static final String OUTPUT_DIRECTORY = "/home/obelix/workspace/documentare-simdoc/simdoc/out";

  @Before
  public void setup() throws IOException {
    cleanup();
    new File(OUTPUT_DIRECTORY).mkdir();
  }

  @After
  public void cleanup() {
    FileUtils.deleteQuietly(new File(OUTPUT_DIRECTORY));
  }

  @Test
  @Ignore // a running simdoc-server is mandatory for this test
  public void remote_build_animals_dna_clustering() throws IOException {
    // Given
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(INPUT_DIRECTORY)
      .outputDirectory(OUTPUT_DIRECTORY)
      .debug()
      .build();

    RemoteClustering remoteClustering = new RemoteClustering();


    // When
    remoteClustering.request("http://localhost:8080", req);

    // Then
    List<String> outputDirectoryList = Arrays.asList(new File(OUTPUT_DIRECTORY).list());
    Assertions.assertThat(outputDirectoryList).contains("metadata.json");
    Assertions.assertThat(outputDirectoryList).contains("clustering-request.json.gz");
    Assertions.assertThat(outputDirectoryList).contains("clustering-graph.json.gz");
    Assertions.assertThat(outputDirectoryList).contains("clustering-result.json.gz");
    Assertions.assertThat(outputDirectoryList).contains("safe-working-dir");
  }
}
