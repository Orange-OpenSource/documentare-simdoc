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
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClusteringTest {
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
    Assertions.assertThat(new File(OUTPUT_DIRECTORY).list()).hasSize(1);
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
    Assertions.assertThat(outputDirectoryList).contains("map.json.gz");
    Assertions.assertThat(outputDirectoryList).contains("clustering-graph.json.gz");
    Assertions.assertThat(outputDirectoryList).contains("clustering-result.json.gz");
  }

  private void coreTest(ClusteringRequest req) throws Exception {
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
    ClusteringRequestResult clusteringRequestResult = toClusteringResult(res);

    Assertions.assertThat(clusteringRequestResult).isEqualTo(expectedClusteringResult());
    Assertions.assertThat(readResultOnDisk()).isEqualTo(expectedClusteringResult());
  }

  private String inputDirectory() throws IOException {
    return context.getResource("classpath:animals-dna").getFile().getAbsolutePath();
  }

  private ClusteringRequestResult expectedClusteringResult() throws IOException {
     return mapper.readValue(
       context.getResource("classpath:expected-clustering-result.json").getFile(),
       ClusteringRequestResult.class
       );
  }

  private ClusteringRequestResult readResultOnDisk() throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
    return (ClusteringRequestResult) jsonGenericHandler.getObjectFromJsonGzipFile(
      ClusteringRequestResult.class,
      new File(OUTPUT_DIRECTORY + "/clustering-result.json.gz")
    );
  }

  private String json(Object req) throws JsonProcessingException {
    return mapper.writeValueAsString(req);
  }

  private ClusteringRequestResult toClusteringResult(MockHttpServletResponse res) throws IOException {
    return mapper.readValue(res.getContentAsString(), ClusteringRequestResult.class);
  }
}
