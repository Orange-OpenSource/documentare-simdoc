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

import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import com.orange.documentare.simdoc.server.biz.RemoteTask;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.CRC32;

import static com.orange.documentare.simdoc.server.biz.clustering.CoreTest.OUTPUT_DIRECTORY;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClusteringGlyphsTest {
  @Autowired
  WebApplicationContext context;

  private CoreTest coreTest;

  @Before
  public void setup() throws IOException {
    coreTest = new CoreTest(context);
  }

  @After
  public void cleanup() {
    coreTest.cleanup();
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
    RemoteTask remoteTask = coreTest.postRequestAndRetrievePendingTaskId(req);
    Assertions.assertThat(remoteTask.id).isNotEmpty();

    return coreTest.waitForRemoteTaskToBeDone(remoteTask);
  }

  private String inputDirectory() throws IOException {
    return context.getResource("classpath:glyphs").getFile().getAbsolutePath();
  }

  private ClusteringRequestResult expectedClusteringResult() throws IOException {
    return coreTest.mapper.readValue(
      context.getResource("classpath:expected-clustering-result-glyphs.json").getFile(),
      ClusteringRequestResult.class
    );
  }
}
