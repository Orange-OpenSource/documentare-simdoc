package com.orange.documentare.simdoc.server.biz.clustering;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    ClusteringResult clusteringResult = toClusteringResult(res);

    Assertions.assertThat(clusteringResult).isEqualTo(expectedClusteringResult());
    Assertions.assertThat(readResultOnDisk()).isEqualTo(expectedClusteringResult());
  }

  private String inputDirectory() throws IOException {
    return context.getResource("classpath:animals-dna").getFile().getAbsolutePath();
  }

  private ClusteringResult expectedClusteringResult() throws IOException {
     return mapper.readValue(
       context.getResource("classpath:expected-clustering-result.json").getFile(),
       ClusteringResult.class
       );
  }

  private ClusteringResult readResultOnDisk() throws IOException {
    return mapper.readValue(
      new File(OUTPUT_DIRECTORY + "/clustering-result.json"),
      ClusteringResult.class
    );
  }

  private String json(Object req) throws JsonProcessingException {
    return mapper.writeValueAsString(req);
  }

  private ClusteringResult toClusteringResult(MockHttpServletResponse res) throws IOException {
    return mapper.readValue(res.getContentAsString(), ClusteringResult.class);
  }
}
