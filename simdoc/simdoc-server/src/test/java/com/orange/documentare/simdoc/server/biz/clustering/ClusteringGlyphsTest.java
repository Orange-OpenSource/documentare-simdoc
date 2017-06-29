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
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.simdoc.server.biz.RemoteTask;
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

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClusteringGlyphsTest {

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
  public void build_glyphs_clustering_with_input_directory() throws Exception {
    // Given
    ClusteringRequest req = ClusteringRequest.builder()
      .inputDirectory(inputDirectory())
      .outputDirectory(OUTPUT_DIRECTORY)
      .debug()
      .build();

    ClusteringRequestResult result = coreTest(req);

    // only result is kept without debug
    Assertions.assertThat(result).isEqualTo(expectedClusteringResult());

    File firstSafeFile = new File(OUTPUT_DIRECTORY + "/safe-working-dir/0");
    CRC32 crc32 = new CRC32();
    crc32.update(FileUtils.readFileToByteArray(firstSafeFile));

    // in raw mode
    Assertions.assertThat(Files.isSymbolicLink(firstSafeFile.toPath())).isFalse();
    Assertions.assertThat(crc32.getValue()).isEqualTo(2789537399L);
  }

  @Test
  public void build_glyphs_clustering_with_bytes_data() throws Exception {
    // Given
    File inputDirectory = new File(inputDirectory());
    BytesData[] bytesData = BytesData.loadFromDirectory(inputDirectory, BytesData.relativePathIdProvider(inputDirectory));
    ClusteringRequest req = ClusteringRequest.builder()
      .bytesData(bytesData)
      .outputDirectory(OUTPUT_DIRECTORY)
      .debug()
      .build();

    ClusteringRequestResult result = coreTest(req);

    // only result is kept without debug
    Assertions.assertThat(result).isEqualTo(expectedClusteringResult());

    File firstSafeFile = new File(OUTPUT_DIRECTORY + "/safe-working-dir/0");
    CRC32 crc32 = new CRC32();
    crc32.update(FileUtils.readFileToByteArray(firstSafeFile));
    // in raw mode
    Assertions.assertThat(Files.isSymbolicLink(firstSafeFile.toPath())).isFalse();
    Assertions.assertThat(crc32.getValue()).isEqualTo(2789537399L);
  }

  private ClusteringRequestResult coreTest(ClusteringRequest req) throws Exception {
    RemoteTask remoteTask = postRequestAndRetrievePendingTaskId(req);
    Assertions.assertThat(remoteTask.id).isNotEmpty();

    return waitForRemoteTaskToBeDone(remoteTask);
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
        .andExpect(status().is2xxSuccessful())
        .andReturn();
    } while (result.getResponse().getStatus() == HttpServletResponse.SC_NO_CONTENT);

    MockHttpServletResponse res = result.getResponse();
    return toClusteringResult(res);
  }

  private String inputDirectory() throws IOException {
    return context.getResource("classpath:glyphs").getFile().getAbsolutePath();
  }

  private ClusteringRequestResult expectedClusteringResult() throws IOException {
    return mapper.readValue(
      context.getResource("classpath:expected-clustering-result-glyphs.json").getFile(),
      ClusteringRequestResult.class
    );
  }

  private String json(Object req) throws JsonProcessingException {
    return mapper.writeValueAsString(req);
  }

  private ClusteringRequestResult toClusteringResult(MockHttpServletResponse res) throws IOException {
    ClusteringRequestResult clusteringRequestResult = mapper.readValue(res.getContentAsString(), ClusteringRequestResult.class);
    return clusteringRequestResult;
  }
}
