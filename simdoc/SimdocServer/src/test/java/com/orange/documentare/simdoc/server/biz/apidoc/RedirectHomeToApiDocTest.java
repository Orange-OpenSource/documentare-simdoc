package com.orange.documentare.simdoc.server.biz.apidoc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.documentare.simdoc.server.biz.clustering.ClusteringRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedirectHomeToApiDocTest {
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
  public void home_redirect_to_swagger_ui() throws Exception {
    // Given
    mockMvc
      // When
      .perform(get("/"))
      // Then
      .andExpect(status().isFound())
      .andExpect(redirectedUrl("swagger-ui.html"));
  }
}
