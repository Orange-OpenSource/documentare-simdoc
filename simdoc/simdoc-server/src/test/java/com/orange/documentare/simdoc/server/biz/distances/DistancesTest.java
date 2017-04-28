package com.orange.documentare.simdoc.server.biz.distances;

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
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Ignore;
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

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DistancesTest {

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
  public void compute_element_to_elements_array_distances() throws Exception {
    // Given
    BytesData element = new BytesData("elem0", new byte[]{1, 2, 3, 4});
    BytesData[] elements = new BytesData[] {
      new BytesData("elem1", new byte[]{5, 6, 7, 8}),
      new BytesData("elem2", new byte[]{1, 2, 3, 4})
    };

    DistancesRequest req = DistancesRequest.builder()
      .element(element)
      .compareTo(elements)
      .build();

    // do
    DistancesRequestResult result = coreTest(req);

    // Then
    Assertions.assertThat(result.distances[0]).isEqualTo(454545);
    Assertions.assertThat(result.distances[1]).isEqualTo(0);
  }

  private DistancesRequestResult coreTest(DistancesRequest req) throws Exception {
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
    return toDistancesResult(res);
  }

  private String json(Object req) throws JsonProcessingException {
    return mapper.writeValueAsString(req);
  }

  private DistancesRequestResult toDistancesResult(MockHttpServletResponse res) throws IOException {
    DistancesRequestResult distancesRequestResult = mapper.readValue(res.getContentAsString(), DistancesRequestResult.class);
    return distancesRequestResult;
  }
}
