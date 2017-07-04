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

import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;


public class RemoteClusteringTest {

  private static final File OUTPUT_DIRECTORY = new File("out");

  private JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();

  @Before
  public void setup() throws IOException {
    cleanup();
    OUTPUT_DIRECTORY.mkdir();
  }

  @After
  public void cleanup() {
    FileUtils.deleteQuietly(OUTPUT_DIRECTORY);
  }

  @Test
  @Ignore // a running simdoc-server is mandatory for this test
  public void remote_build_animals_dna_clustering() throws IOException {
    // Given
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(inputDirectory())
      .outputDirectory(OUTPUT_DIRECTORY.getAbsolutePath())
      .debug()
      .build();

    RemoteClustering remoteClustering = new RemoteClustering();

    // When
    ClusteringRequestResult result = remoteClustering.request("http://localhost:8080", req);

    // Then
    Assertions.assertThat(result).isEqualTo(expectedClusteringResult(false));
  }

  @Test
  @Ignore // a running simdoc-server is mandatory for this test
  // FIXME bug with bytes data if only files are provided
  public void remote_build_animals_dna_clustering_with_bytes_data() throws IOException {
    // Given
    ClusteringRequest req = ClusteringRequest.builder()
      .bytesData(bytesData())
      .outputDirectory(OUTPUT_DIRECTORY.getAbsolutePath())
      .debug()
      .build();

    RemoteClustering remoteClustering = new RemoteClustering();

    // When
    ClusteringRequestResult result = remoteClustering.request("http://localhost:8080", req);

    // Then
    ClusteringRequestResult expected = expectedClusteringResult(true);
    Assertions.assertThat(result).isEqualTo(expected);
  }

  @Test
  @Ignore // a running simdoc-server is mandatory for this test
  public void remote_build_animals_dna_clustering_with_bytes_data_with_bytes_array() throws IOException {
    // Given
    ClusteringRequest req = ClusteringRequest.builder()
      .bytesData(bytesDataWithBytes().bytesData)
      .outputDirectory(OUTPUT_DIRECTORY.getAbsolutePath())
      .debug()
      .build();

    RemoteClustering remoteClustering = new RemoteClustering();

    // When
    ClusteringRequestResult result = remoteClustering.request("http://localhost:8080", req);

    // Then
    ClusteringRequestResult expected = expectedClusteringResult(true);
    Assertions.assertThat(result).isEqualTo(expected);
  }

  private BytesData[] bytesData() {
    return BytesData.loadFromDirectory(new File(inputDirectory()), file -> file.getName());
  }

  private BytesDataArray bytesDataWithBytes() throws IOException {
    return (BytesDataArray)jsonGenericHandler.getObjectFromJsonFile(BytesDataArray.class, new File(getClass().getResource("/bytes-data-animals-dna.json").getFile()));
  }

  private ClusteringRequestResult expectedClusteringResult(boolean bytesDataMode) throws IOException {
    File file = new File(getClass().getResource(
        bytesDataMode ? "/expected-clustering-result-bytes-data.json" : "/expected-clustering-result.json").getFile());
    return (ClusteringRequestResult) jsonGenericHandler.getObjectFromJsonFile(ClusteringRequestResult.class, file);
  }

  private String inputDirectory() {
    URL resource = getClass().getResource("/animals-dna");
    return new File(resource.getFile()).getAbsolutePath();
  }
}
