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
import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.simdoc.server.biz.FileIO;
import com.orange.documentare.simdoc.server.biz.SharedDirectory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClusteringParametersTest {
  private static final String INPUT_DIRECTORY = "in";
  private static final String OUTPUT_DIRECTORY = "out";

  private final ObjectMapper mapper = new ObjectMapper();

  @RequiredArgsConstructor
  private class ExpectedParameters {
    public final ClusteringParameters clusteringParameters;
    public final boolean debug;
  }

  @Autowired
  WebApplicationContext context;

  @Autowired
  SharedDirectory sharedDirectory;

  @MockBean
  ClusteringService clusteringService;

  MockMvc mockMvc;

  @Before
  public void setup() throws IOException {
    cleanup();

    mockMvc = MockMvcBuilders
      .webAppContextSetup(context)
      .alwaysDo(print())
      .build();
  }

  @After
  public void cleanup() {
    FileUtils.deleteQuietly(new File(INPUT_DIRECTORY));
    FileUtils.deleteQuietly(new File(OUTPUT_DIRECTORY));
  }

  @Test
  public void call_service_with_default_parameters() throws Exception {
    // Given
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(INPUT_DIRECTORY)
      .outputDirectory(OUTPUT_DIRECTORY)
      .build();

    ExpectedParameters expectedParameters =
      new ExpectedParameters(ClusteringParameters.builder().build(), false);

    // When/Then
    test(req, expectedParameters);
  }

  @Test
  public void call_service_with_debug() throws Exception {
    // Given
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(INPUT_DIRECTORY)
      .outputDirectory(OUTPUT_DIRECTORY)
      .debug()
      .build();

    ExpectedParameters expectedParameters =
      new ExpectedParameters(ClusteringParameters.builder().build(), true);

    // When/Then
    test(req, expectedParameters);
  }

  @Test
  public void call_service_with_parameters() throws Exception {
    // Given
    float acut = 1.1f;
    float qcut = 2.1f;
    float scut = 3.1f;
    int ccut = 4;
    int k = 6;
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(INPUT_DIRECTORY)
      .outputDirectory(OUTPUT_DIRECTORY)
      .acut(acut)
      .qcut(qcut)
      .scut(scut)
      .ccut(ccut)
      .wcut()
      .k(k)
      .sloop()
      .build();

    ExpectedParameters expectedParameters = new ExpectedParameters(
      ClusteringParameters.builder()
        .acut(acut)
        .qcut(qcut)
        .scut(scut)
        .ccut(ccut)
        .wcut()
        .knn(k)
        .sloop()
        .build(), false);

    // When/Then
    test(req, expectedParameters);
  }

  private void test(ClusteringRequest req, ExpectedParameters expectedParameters) throws Exception {
    createInputDirectory();
    createOutputDirectory();

    mockMvc
      // When
      .perform(
        post("/clustering")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(req)))
      // Then
      .andExpect(status().isOk());

    Mockito.verify(clusteringService).build(
      new FileIO(sharedDirectory, req),
      req);
  }

  private String json(Object req) throws JsonProcessingException {
    return mapper.writeValueAsString(req);
  }

  private void createInputDirectory() {
    (new File(INPUT_DIRECTORY)).mkdir();
  }
  private void createOutputDirectory() {
    (new File(OUTPUT_DIRECTORY)).mkdir();
  }
}
