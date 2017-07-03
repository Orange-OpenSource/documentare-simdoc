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
import com.orange.documentare.simdoc.server.biz.RemoteTask;
import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
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

public class CoreTest {

  static final String OUTPUT_DIRECTORY = "out";

  final ObjectMapper mapper = new ObjectMapper();

  private MockMvc mockMvc;

  CoreTest(WebApplicationContext context) throws IOException {
    cleanup();
    new File(OUTPUT_DIRECTORY).mkdir();

    mockMvc = MockMvcBuilders
      .webAppContextSetup(context)
      .alwaysDo(print())
      .build();
  }

  void cleanup() {
    FileUtils.deleteQuietly(new File(OUTPUT_DIRECTORY));
  }

  RemoteTask postRequestAndRetrievePendingTaskId(ClusteringRequest req) throws Exception {
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

  ClusteringRequestResult waitForRemoteTaskToBeDone(RemoteTask remoteTask) throws Exception {
    MvcResult result;
    do {
      result = mockMvc
        // When
        .perform(
          get("/task/" + remoteTask.id))
        // Then
        .andExpect(status().is2xxSuccessful())
        .andReturn();
      Thread.sleep(500);
    } while (result.getResponse().getStatus() == HttpServletResponse.SC_NO_CONTENT);

    MockHttpServletResponse res = result.getResponse();
    return toClusteringResult(res);
  }

  private String json(Object req) throws JsonProcessingException {
    return mapper.writeValueAsString(req);
  }

  private ClusteringRequestResult toClusteringResult(MockHttpServletResponse res) throws IOException {
    ClusteringRequestResult clusteringRequestResult = mapper.readValue(res.getContentAsString(), ClusteringRequestResult.class);
    return clusteringRequestResult;
  }
}
