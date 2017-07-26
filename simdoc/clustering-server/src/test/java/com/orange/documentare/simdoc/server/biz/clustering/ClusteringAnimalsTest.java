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

import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.simdoc.server.biz.RemoteTask;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.orange.documentare.simdoc.server.biz.clustering.CoreTest.OUTPUT_DIRECTORY;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClusteringAnimalsTest {

  @Autowired
  WebApplicationContext context;

  private CoreTest coreTest;

  @Before
  public void setup() throws IOException {
    coreTest = new CoreTest(context);
  }

  @After
  public void cleanup() {
    coreTest.cleanup();
  }


  @Test
  public void build_animals_dna_clustering_with_bytes_data_bytes_in_debug_mode() throws Exception {
    // Given
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
    File bytesDataJson = new File(getClass().getResource("/bytes-data-bytes-animals-dna.json").getFile());
    BytesDataArray bytesDataArray = (BytesDataArray) jsonGenericHandler.getObjectFromJsonFile(BytesDataArray.class, bytesDataJson);

    ClusteringRequest req = ClusteringRequest.builder()
      .bytesData(bytesDataArray.bytesData)
      .outputDirectory(OUTPUT_DIRECTORY)
      .debug()
      .build();

    coreTest(req, "expected-clustering-result-bytes-data-animals-dna.json");

    List<String> outputDirectoryList = Arrays.asList(new File(OUTPUT_DIRECTORY).list());
    Assertions.assertThat(outputDirectoryList).contains("clustering-request.json.gz");
    Assertions.assertThat(outputDirectoryList).contains("clustering-graph.json.gz");
    Assertions.assertThat(outputDirectoryList).contains("clustering-result.json.gz");
  }

  @Test
  public void build_animals_dna_clustering_with_bytes_data_files_in_debug_mode() throws Exception {
    // Given
    BytesData[] bytesData = BytesData.loadFromDirectory(new File(inputDirectory()), File::getName);

    ClusteringRequest req = ClusteringRequest.builder()
      .bytesData(bytesData)
      .outputDirectory(OUTPUT_DIRECTORY)
      .debug()
      .build();

    coreTest(req, "expected-clustering-result-bytes-data-animals-dna.json");

    List<String> outputDirectoryList = Arrays.asList(new File(OUTPUT_DIRECTORY).list());
    Assertions.assertThat(outputDirectoryList).contains("clustering-request.json.gz");
    Assertions.assertThat(outputDirectoryList).contains("clustering-graph.json.gz");
    Assertions.assertThat(outputDirectoryList).contains("clustering-result.json.gz");
  }

  private void coreTest(ClusteringRequest req, String expectedJson) throws Exception {
    RemoteTask remoteTask = coreTest.postRequestAndRetrievePendingTaskId(req);
    Assertions.assertThat(remoteTask.id).isNotEmpty();

    ClusteringRequestResult result = coreTest.waitForRemoteTaskToBeDone(remoteTask);

    Assertions.assertThat(result).isEqualTo(expectedClusteringResult(expectedJson));
    Assertions.assertThat(readResultOnDisk()).isEqualTo(expectedClusteringResult(expectedJson));
  }

  private String inputDirectory() throws IOException {
    return context.getResource("classpath:animals-dna").getFile().getAbsolutePath();
  }

  private ClusteringRequestResult expectedClusteringResult(String expectedJson) throws IOException {
    return coreTest.mapper.readValue(context.getResource("classpath:" + expectedJson).getFile(), ClusteringRequestResult.class);
  }

  private ClusteringRequestResult readResultOnDisk() throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
    ClusteringRequestResult clusteringRequestResult = (ClusteringRequestResult) jsonGenericHandler.getObjectFromJsonGzipFile(
      ClusteringRequestResult.class,
      new File(OUTPUT_DIRECTORY + "/clustering-result.json.gz")
    );
    return clusteringRequestResult;
  }
}
