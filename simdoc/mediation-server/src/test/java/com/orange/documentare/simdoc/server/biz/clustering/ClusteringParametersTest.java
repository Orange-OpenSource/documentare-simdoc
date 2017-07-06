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
import com.orange.documentare.simdoc.server.biz.RemoteTask;
import com.orange.documentare.simdoc.server.biz.SharedDirectory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    // When/Then
    test(req);
  }

  @Test
  public void call_service_with_debug() throws Exception {
    // Given
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(INPUT_DIRECTORY)
      .outputDirectory(OUTPUT_DIRECTORY)
      .debug()
      .build();

    // When/Then
    test(req);
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

    // When/Then
    test(req);
  }

  private void test(ClusteringRequest req) throws Exception {
    createInputDirectory();
    createOutputDirectory();

    RemoteTask remoteTask = postRequestAndRetrievePendingTaskId(req);
    waitForRemoteTaskToBeDone(remoteTask);
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
        .andReturn();
    } while (result.getResponse().getStatus() == HttpServletResponse.SC_NO_CONTENT);

    MockHttpServletResponse res = result.getResponse();
    return toClusteringResult(res);
  }
  private ClusteringRequestResult toClusteringResult(MockHttpServletResponse res) throws IOException {
    ClusteringRequestResult clusteringRequestResult = mapper.readValue(res.getContentAsString(), ClusteringRequestResult.class);
    return clusteringRequestResult;
  }
}
