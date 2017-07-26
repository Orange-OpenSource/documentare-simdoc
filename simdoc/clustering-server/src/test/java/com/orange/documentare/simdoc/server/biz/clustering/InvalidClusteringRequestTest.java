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
import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvalidClusteringRequestTest {
  private static final String OUTPUT_DIRECTORY = "out";

  private final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  WebApplicationContext context;

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
    FileUtils.deleteQuietly(new File(OUTPUT_DIRECTORY));
  }

  @Test
  public void clustering_api_return_bad_request_if_body_is_empty() throws Exception {
    // Given
    String expectedMessage = "Request body not readable";
    test(null, expectedMessage);
  }

  @Test
  public void clustering_api_return_bad_request_if_bytes_data_is_missing() throws Exception {
    // Given
    String expectedMessage = "bytesData is missing";
    ClusteringRequest req = ClusteringRequest.builder()
      .outputDirectory(OUTPUT_DIRECTORY)
      .build();

    test(req, expectedMessage);
  }



  @Test
  public void clustering_api_return_bad_request_if_output_directory_is_missing() throws Exception {
    // Given
    String expectedMessage = "outputDirectory is missing";
    BytesData[] bytesData = BytesData.loadFromDirectory(new File(inputDirectory()), File::getName);
    ClusteringRequest req = ClusteringRequest.builder()
      .bytesData(bytesData)
      .build();

    test(req, expectedMessage);
  }

  @Test
  public void clustering_api_return_bad_request_if_output_directory_is_not_a_directory() throws Exception {
    // Given
    String expectedMessage = "outputDirectory is not a directory: ";
    FileUtils.writeStringToFile(new File(OUTPUT_DIRECTORY), "hi");
    BytesData[] bytesData = BytesData.loadFromDirectory(new File(inputDirectory()), File::getName);
    ClusteringRequest req = ClusteringRequest.builder()
      .bytesData(bytesData)
      .outputDirectory(OUTPUT_DIRECTORY)
      .build();

    test(req, expectedMessage);
  }

  @Test
  public void clustering_api_return_bad_request_if_output_directory_is_not_writable() throws Exception {
    // Given
    BytesData[] bytesData = BytesData.loadFromDirectory(new File(inputDirectory()), File::getName);
    String expectedMessage = "outputDirectory is not writable:";
    ClusteringRequest req = ClusteringRequest.builder()
      .bytesData(bytesData)
      .outputDirectory("/")
      .build();

    test(req, expectedMessage);
  }

  @Test
  public void clustering_api_return_bad_request_if_request_contains_unknown_field() throws Exception {
    // Given
    String expectedMessage = "Request body not readable";
    test(null, expectedMessage);
  }

  private void test(ClusteringRequest req, String expectedMessage) throws Exception {
    MvcResult result = mockMvc
      // When
      .perform(
        post("/clustering")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(req)))
      // Then
      .andExpect(status().isBadRequest())
      .andReturn();
    Assertions.assertThat(result.getResponse().getErrorMessage()).contains(expectedMessage);
  }

  private String json(Object req) throws JsonProcessingException {
    return mapper.writeValueAsString(req);
  }

  private String inputDirectory() throws IOException {
    return context.getResource("classpath:animals-dna").getFile().getAbsolutePath();
  }
}
