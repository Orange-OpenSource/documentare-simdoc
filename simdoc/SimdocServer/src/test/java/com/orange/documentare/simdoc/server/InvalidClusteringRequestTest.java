package com.orange.documentare.simdoc.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    FileUtils.deleteQuietly(new File(INPUT_DIRECTORY));
    FileUtils.deleteQuietly(new File(OUTPUT_DIRECTORY));

    mockMvc = MockMvcBuilders
      .webAppContextSetup(context)
      .alwaysDo(print())
      .build();
  }

  @Test
  public void clustering_api_return_bad_request_if_body_is_empty() throws Exception {
    // Given
    mockMvc
      // When
      .perform(
        post("/clustering")
          .contentType(MediaType.APPLICATION_JSON))
      // Then
      .andExpect(status().isBadRequest());
  }

  @Test
  public void clustering_api_return_bad_request_if_input_directory_is_missing() throws Exception {
    // Given
    ClusteringRequest req = ClusteringRequest.builder()
      .outputDirectory(OUTPUT_DIRECTORY)
      .build();

    MvcResult result = mockMvc
      // When
      .perform(
        post("/clustering")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(req)))
      // Then
      .andExpect(status().isBadRequest())
      .andReturn();

    String json = result.getResponse().getContentAsString();
    Assertions.assertThat(json).contains("inputDirectory is missing");
  }

  @Test
  public void clustering_api_return_bad_request_if_input_directory_is_not_reachable() throws Exception {
    // Given
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory("/xxx")
      .outputDirectory(OUTPUT_DIRECTORY)
      .build();

    MvcResult result = mockMvc
      // When
      .perform(
        post("/clustering")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(req)))
      // Then
      .andExpect(status().isBadRequest())
      .andReturn();

    String json = result.getResponse().getContentAsString();
    Assertions.assertThat(json).contains("inputDirectory can not be reached: /xxx");
  }

  @Test
  public void clustering_api_return_bad_request_if_input_directory_is_not_a_directory() throws Exception {
    // Given
    FileUtils.writeStringToFile(new File(INPUT_DIRECTORY), "hi");
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(INPUT_DIRECTORY)
      .outputDirectory(OUTPUT_DIRECTORY)
      .build();

    MvcResult result = mockMvc
      // When
      .perform(
        post("/clustering")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(req)))
      // Then
      .andExpect(status().isBadRequest())
      .andReturn();

    String json = result.getResponse().getContentAsString();
    Assertions.assertThat(json).contains("inputDirectory is not a directory: in");
  }

  @Test
  public void clustering_api_return_bad_request_if_output_directory_is_missing() throws Exception {
    // Given
    createInputDirectory();
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(INPUT_DIRECTORY)
      .build();

    MvcResult result = mockMvc
      // When
      .perform(
        post("/clustering")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(req)))
      // Then
      .andExpect(status().isBadRequest())
      .andReturn();

    String json = result.getResponse().getContentAsString();
    Assertions.assertThat(json).contains("outputDirectory is missing");
  }

  @Test
  public void clustering_api_return_bad_request_if_output_directory_is_not_a_directory() throws Exception {
    // Given
    createInputDirectory();
    FileUtils.writeStringToFile(new File(OUTPUT_DIRECTORY), "hi");
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(INPUT_DIRECTORY)
      .outputDirectory(OUTPUT_DIRECTORY)
      .build();

    MvcResult result = mockMvc
      // When
      .perform(
        post("/clustering")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(req)))
      // Then
      .andExpect(status().isBadRequest())
      .andReturn();

    String json = result.getResponse().getContentAsString();
    Assertions.assertThat(json).contains("outputDirectory is not a directory: out");
  }

  @Test
  public void clustering_api_return_bad_request_if_output_directory_is_not_writable() throws Exception {
    // Given
    createInputDirectory();
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(INPUT_DIRECTORY)
      .outputDirectory("/")
      .build();

    MvcResult result = mockMvc
      // When
      .perform(
        post("/clustering")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(req)))
      // Then
      .andExpect(status().isBadRequest())
      .andReturn();

    String json = result.getResponse().getContentAsString();
    Assertions.assertThat(json).contains("outputDirectory is not writable: /");
  }

  @Test
  public void clustering_api_return_bad_request_if_parameters_are_missing() throws Exception {
    // Given
    createInputDirectory();
    createOutputDirectory();
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(INPUT_DIRECTORY)
      .outputDirectory(OUTPUT_DIRECTORY)
      .parameters(null)
      .build();

    MvcResult result = mockMvc
      // When
      .perform(
        post("/clustering")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(req)))
      // Then
      .andExpect(status().isBadRequest())
      .andReturn();

    String json = result.getResponse().getContentAsString();
    Assertions.assertThat(json).contains("parameters are missing");
  }

  private String json(ClusteringRequest req) throws JsonProcessingException {
    return mapper.writeValueAsString(req);
  }

  private void createInputDirectory() {
    (new File(INPUT_DIRECTORY)).mkdir();
  }
  private void createOutputDirectory() {
    (new File(OUTPUT_DIRECTORY)).mkdir();
  }
}
