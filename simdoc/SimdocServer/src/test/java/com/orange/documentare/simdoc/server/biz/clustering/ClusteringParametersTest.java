package com.orange.documentare.simdoc.server.biz.clustering;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
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

  @MockBean
  ClusteringService clusteringService;

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
      .build();

    ExpectedParameters expectedParameters = new ExpectedParameters(
      ClusteringParameters.builder()
        .acut(acut)
        .qcut(qcut)
        .scut(scut)
        .ccut(ccut)
        .wcut()
        .knn(k)
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
      INPUT_DIRECTORY,
      OUTPUT_DIRECTORY,
      expectedParameters.clusteringParameters,
      expectedParameters.debug);
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
