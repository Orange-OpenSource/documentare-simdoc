package com.orange.documentare.core.prepdata;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Denis Boisset & Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.prepdata.PrepData;
import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Test;

import java.io.File;

public class PrepDataTest {

  private static File PREP_DATA_JSON = new File("prepped-data.json.gz");
  private static File META_DATA_JSON = new File("meta-data.json.gz");

  @After
  public void cleanup() {
    FileUtils.deleteQuietly(PREP_DATA_JSON);
    FileUtils.deleteQuietly(META_DATA_JSON);
  }

  @Test
  public void process_input_directory_and_prep_bytes_data_based_json() {
    // Given
    File inputDirectory = new File(getClass().getResource("/prep-data-test-input-dir").getFile());
    PrepData prepData = PrepData.builder()
      .inputDirectory(inputDirectory)
      .preppedDataOutputFile(PREP_DATA_JSON)
      .metadataOutputFile(META_DATA_JSON)
      .build();

    // When
    prepData.prep();

    // Then
    Assertions.assertThat(PREP_DATA_JSON.exists()).isTrue();
    Assertions.assertThat(META_DATA_JSON.exists()).isTrue();
  }
}
