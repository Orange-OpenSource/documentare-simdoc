package com.orange.documentare.simdoc.server.biz.task;
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
import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import com.orange.documentare.simdoc.server.biz.RemoteTask;
import com.orange.documentare.simdoc.server.biz.distances.DistancesRequest;
import com.orange.documentare.simdoc.server.biz.distances.DistancesRequestResult;
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
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TasksResetTest {

  private static final String[] ANIMALS = {
    "pigmyChimpanzee", "chimpanzee", "platypus", "human"
  };

  private final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  WebApplicationContext context;

  MockMvc mockMvc;

  @Before
  public void setup() throws IOException {
    mockMvc = MockMvcBuilders
      .webAppContextSetup(context)
      .alwaysDo(print())
      .build();
  }

  @Test
  public void kill_all_tasks() throws Exception {
    // Given
    BytesData element = load("human");
    BytesData[] elements = loadAnimals();

    DistancesRequest req = DistancesRequest.builder()
      .element(element)
      .compareTo(elements)
      .build();

    RemoteTask remoteTask = postRequestAndRetrievePendingTaskId(req);
    // do
    killAll();

    // Then
    remoteTaskDoesNotExist(remoteTask);
  }

  private BytesData load(String id) {
    File file = new File(getClass().getResource("/animals-dna/" + id).getFile());
    return new BytesData(id, file.getAbsolutePath());
  }

  private BytesData[] loadAnimals() {
    return Arrays.stream(ANIMALS)
      .map(this::load)
      .collect(Collectors.toList())
    .toArray(new BytesData[ANIMALS.length]);
  }

  private RemoteTask postRequestAndRetrievePendingTaskId(DistancesRequest req) throws Exception {
    MvcResult result = mockMvc
      // When
      .perform(
        post("/distances")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(req)))
      // Then
      .andExpect(status().isOk())
      .andReturn();

    MockHttpServletResponse res = result.getResponse();
    return toRemoteTask(res);
  }

  private void killAll() throws Exception {
    mockMvc
      // When
      .perform(
        post("/kill-all-tasks"))
      // Then
      .andExpect(status().isOk())
      .andReturn();
  }

  private void remoteTaskDoesNotExist(RemoteTask remoteTask) throws Exception {
    mockMvc
        // When
        .perform(
          get("/task/" + remoteTask.id))
        // Then
        .andExpect(status().isNotFound())
        .andReturn();
  }

  private String json(Object req) throws JsonProcessingException {
    return mapper.writeValueAsString(req);
  }

  private RemoteTask toRemoteTask(MockHttpServletResponse res) throws IOException {
    RemoteTask remoteTask = mapper.readValue(res.getContentAsString(), RemoteTask.class);
    return remoteTask;
  }
}
