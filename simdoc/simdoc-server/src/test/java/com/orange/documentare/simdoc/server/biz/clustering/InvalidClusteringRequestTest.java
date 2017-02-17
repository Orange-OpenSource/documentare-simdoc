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
import org.springframework.mock.web.MockHttpServletResponse;
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
  private static final String INPUT_DIRECTORY = "in";
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
    FileUtils.deleteQuietly(new File(INPUT_DIRECTORY));
    FileUtils.deleteQuietly(new File(OUTPUT_DIRECTORY));
  }

  @Test
  public void clustering_api_return_bad_request_if_body_is_empty() throws Exception {
    // Given
    String expectedMessage = "Request body not readable";
    test(null, expectedMessage);
  }

  @Test
  public void clustering_api_return_bad_request_if_input_directory_and_bytes_data_are_missing() throws Exception {
    // Given
    String expectedMessage = "inputDirectory and bytesData are missing";
    ClusteringRequest req = ClusteringRequest.builder()
      .outputDirectory(OUTPUT_DIRECTORY)
      .build();

    test(req, expectedMessage);
  }

  @Test
  public void clustering_api_return_bad_request_if_input_directory_is_not_reachable() throws Exception {
    // Given
    String expectedMessage = "inputDirectory can not be reached: /xxx";
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory("/xxx")
      .outputDirectory(OUTPUT_DIRECTORY)
      .build();

    test(req, expectedMessage);
  }

  @Test
  public void clustering_api_return_bad_request_if_input_directory_is_not_a_directory() throws Exception {
    // Given
    String expectedMessage = "inputDirectory is not a directory: ";
    FileUtils.writeStringToFile(new File(INPUT_DIRECTORY), "hi");
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(INPUT_DIRECTORY)
      .outputDirectory(OUTPUT_DIRECTORY)
      .build();

    test(req, expectedMessage);
  }

  @Test
  public void clustering_api_return_bad_request_if_output_directory_is_missing() throws Exception {
    // Given
    String expectedMessage = "outputDirectory is missing";
    createInputDirectory();
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(INPUT_DIRECTORY)
      .build();

    test(req, expectedMessage);
  }

  @Test
  public void clustering_api_return_bad_request_if_output_directory_is_not_a_directory() throws Exception {
    // Given
    String expectedMessage = "outputDirectory is not a directory: ";
    createInputDirectory();
    FileUtils.writeStringToFile(new File(OUTPUT_DIRECTORY), "hi");
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(INPUT_DIRECTORY)
      .outputDirectory(OUTPUT_DIRECTORY)
      .build();

    test(req, expectedMessage);
  }

  @Test
  public void clustering_api_return_bad_request_if_output_directory_is_not_writable() throws Exception {
    // Given
    String expectedMessage = "outputDirectory is not writable:";
    createInputDirectory();
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(INPUT_DIRECTORY)
      .outputDirectory("/")
      .build();

    test(req, expectedMessage);
  }

  @Test
  public void clustering_api_return_bad_request_if_request_contains_unknown_field() throws Exception {
    // Given
    String expectedMessage = "Request JSON body contains an unknown property";
    test(new InvalidRequest(), expectedMessage);
  }

  private void test(Object req, String expectedMessage) throws Exception {
    MvcResult result = mockMvc
      // When
      .perform(
        post("/clustering")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(req)))
      // Then
      .andExpect(status().isBadRequest())
      .andReturn();

    MockHttpServletResponse res = result.getResponse();
    ClusteringRequestResult clusteringRequestResult = toClusteringResult(res);
    Assertions.assertThat(res.getErrorMessage()).contains(expectedMessage);
    Assertions.assertThat(clusteringRequestResult.error).isTrue();
    Assertions.assertThat(clusteringRequestResult.errorMessage).contains(expectedMessage);
  }

  private String json(Object req) throws JsonProcessingException {
    return mapper.writeValueAsString(req);
  }

  private ClusteringRequestResult toClusteringResult(MockHttpServletResponse res) throws IOException {
    return mapper.readValue(res.getContentAsString(), ClusteringRequestResult.class);
  }

  private void createInputDirectory() {
    (new File(INPUT_DIRECTORY)).mkdir();
  }

  private class InvalidRequest {
    public int x;
  }
}
