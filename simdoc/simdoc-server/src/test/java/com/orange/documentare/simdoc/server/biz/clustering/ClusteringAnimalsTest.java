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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.documentare.core.comp.ncd.Ncd;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.simdoc.server.biz.RemoteTask;
import com.orange.documentare.simdoc.server.biz.distances.DistancesRequestResult;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClusteringAnimalsTest {

  private static final String OUTPUT_DIRECTORY = "out";

  private final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  WebApplicationContext context;

  MockMvc mockMvc;

  @Before
  public void setup() throws IOException {
    cleanup();
    new File(OUTPUT_DIRECTORY).mkdir();

    mockMvc = MockMvcBuilders
      .webAppContextSetup(context)
      .alwaysDo(print())
      .build();
  }

  @After
  public void cleanup() {
    FileUtils.deleteQuietly(new File(OUTPUT_DIRECTORY));
  }

  @Test
  public void build_animals_dna_clustering() throws Exception {
    // Given
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(inputDirectory())
      .outputDirectory(OUTPUT_DIRECTORY)
      .build();

    coreTest(req);

    // only result is kept without debug
    Assertions.assertThat(new File(OUTPUT_DIRECTORY).list()).hasSize(2);
  }

  @Test
  public void build_animals_dna_clustering_in_debug_mode() throws Exception {
    // Given
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(inputDirectory())
      .outputDirectory(OUTPUT_DIRECTORY)
      .debug()
      .build();

    coreTest(req);

    List<String> outputDirectoryList = Arrays.asList(new File(OUTPUT_DIRECTORY).list());
    Assertions.assertThat(outputDirectoryList).contains("metadata.json");
    Assertions.assertThat(outputDirectoryList).contains("clustering-request.json.gz");
    Assertions.assertThat(outputDirectoryList).contains("clustering-graph.json.gz");
    Assertions.assertThat(outputDirectoryList).contains("clustering-result.json.gz");
    Assertions.assertThat(outputDirectoryList).contains("safe-working-dir");
  }

  @Test
  public void build_animals_dna_clustering_with_bytes_data_in_debug_mode() throws Exception {
    // Given
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
    File bytesDataJson = new File(getClass().getResource("/bytes-data-animals-dna.json").getFile());
    BytesDataArray bytesDatasArray = (BytesDataArray) jsonGenericHandler.getObjectFromJsonFile(BytesDataArray.class, bytesDataJson);

    ClusteringRequest req = ClusteringRequest.builder()
      .bytesData(bytesDatasArray.bytesData)
      .outputDirectory(OUTPUT_DIRECTORY)
      .debug()
      .build();

    coreTest(req);

    List<String> outputDirectoryList = Arrays.asList(new File(OUTPUT_DIRECTORY).list());
    Assertions.assertThat(outputDirectoryList).contains("clustering-request.json.gz");
    Assertions.assertThat(outputDirectoryList).contains("clustering-graph.json.gz");
    Assertions.assertThat(outputDirectoryList).contains("clustering-result.json.gz");
  }

  private void coreTest(ClusteringRequest req) throws Exception {
    RemoteTask remoteTask = postRequestAndRetrievePendingTaskId(req);
    Assertions.assertThat(remoteTask.id).isNotEmpty();

    ClusteringRequestResult result = waitForRemoteTaskToBeDone(remoteTask);
    Assertions.assertThat(result).isEqualTo(expectedClusteringResult(req.bytesDataMode));
    Assertions.assertThat(readResultOnDisk()).isEqualTo(expectedClusteringResult(req.bytesDataMode));
  }

  private RemoteTask postRequestAndRetrievePendingTaskId(ClusteringRequest req) throws Exception {
    MvcResult result = mockMvc
      // When
      .perform(
        post("/clustering")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(req)))
      // Then
      .andExpect(status().isOk())
      .andReturn();

    MockHttpServletResponse res = result.getResponse();
    return toRemoteTask(res);
  }

  private RemoteTask toRemoteTask(MockHttpServletResponse res) throws IOException {
    RemoteTask remoteTask = mapper.readValue(res.getContentAsString(), RemoteTask.class);
    return remoteTask;
  }

  private ClusteringRequestResult waitForRemoteTaskToBeDone(RemoteTask remoteTask) throws Exception {
    MvcResult result;
    do {
      result = mockMvc
        // When
        .perform(
          get("/task/" + remoteTask.id))
        // Then
        .andExpect(status().is2xxSuccessful())
        .andReturn();
    } while (result.getResponse().getStatus() == HttpServletResponse.SC_NO_CONTENT);

    MockHttpServletResponse res = result.getResponse();
    return toClusteringResult(res);
  }

  private String inputDirectory() throws IOException {
    return context.getResource("classpath:animals-dna").getFile().getAbsolutePath();
  }

  private ClusteringRequestResult expectedClusteringResult(boolean bytesDataMode) throws IOException {
    return mapper.readValue(
      context.getResource(
        bytesDataMode ? "classpath:expected-clustering-result-bytes-data-animals-dna.json" : "classpath:expected-clustering-result-animals-dna.json").getFile(),
      ClusteringRequestResult.class
    );
  }

  private ClusteringRequestResult readResultOnDisk() throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
    ClusteringRequestResult clusteringRequestResult = (ClusteringRequestResult) jsonGenericHandler.getObjectFromJsonGzipFile(
      ClusteringRequestResult.class,
      new File(OUTPUT_DIRECTORY + "/clustering-result.json.gz")
    );
    return clusteringRequestResult;
  }

  private String json(Object req) throws JsonProcessingException {
    return mapper.writeValueAsString(req);
  }

  private ClusteringRequestResult toClusteringResult(MockHttpServletResponse res) throws IOException {
    ClusteringRequestResult clusteringRequestResult = mapper.readValue(res.getContentAsString(), ClusteringRequestResult.class);
    return clusteringRequestResult;
  }
}