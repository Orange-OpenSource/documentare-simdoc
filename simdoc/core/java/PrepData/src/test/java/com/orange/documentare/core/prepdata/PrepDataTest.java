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

import com.orange.documentare.core.model.json.JsonGenericHandler;
import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class PrepDataTest {
  private static File PREPPED_DATA_JSON = new File("prepped-data.json");
  private static File METADATA_JSON = new File("metadata.json");

  @After
  public void cleanup() {
    FileUtils.deleteQuietly(PREPPED_DATA_JSON);
    FileUtils.deleteQuietly(METADATA_JSON);
  }

  @Test(expected = IllegalStateException.class)
  public void raise_exception_if_input_directory_does_not_exists() {
    PrepData.builder()
      .inputDirectory(new File("/pouet-pouet"))
      .preppedDataOutputFile(PREPPED_DATA_JSON)
      .metadataOutputFile(METADATA_JSON)
      .build();
  }

  @Test(expected = IllegalStateException.class)
  public void raise_exception_if_prepped_data_output_file_is_null() {
    File inputDirectory = new File(getClass().getResource("/prep-data-test-input-dir").getFile());
    PrepData.builder()
      .inputDirectory(inputDirectory)
      .preppedDataOutputFile(null)
      .metadataOutputFile(METADATA_JSON)
      .build();
  }

  @Test(expected = IllegalStateException.class)
  public void raise_exception_if_metadata_output_file_is_null() {
    File inputDirectory = new File(getClass().getResource("/prep-data-test-input-dir").getFile());
    PrepData.builder()
      .inputDirectory(inputDirectory)
      .preppedDataOutputFile(PREPPED_DATA_JSON)
      .metadataOutputFile(null)
      .build();
  }

  @Test
  public void process_input_directory_and_prep_bytes_data_based_json() throws IOException {
    // Given
    File inputDirectory = new File(getClass().getResource("/prep-data-test-input-dir").getFile());
    PrepData prepData = PrepData.builder()
      .inputDirectory(inputDirectory)
      .preppedDataOutputFile(PREPPED_DATA_JSON)
      .metadataOutputFile(METADATA_JSON)
      .build();

    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
    // When
    prepData.prep();
    PreppedData preppedData =
      (PreppedData) jsonGenericHandler.getObjectFromJsonFile(PreppedData.class, PREPPED_DATA_JSON);
    Metadata metadata = (Metadata) jsonGenericHandler.getObjectFromJsonFile(Metadata.class, METADATA_JSON);

    // Then
    Assertions.assertThat(preppedData.bytesData.length).isEqualTo(2);
    Assertions.assertThat(preppedData.bytesData[0].id).isEqualTo("image.png");
    Assertions.assertThat(preppedData.bytesData[1].id).isEqualTo("subdir/opossum");
    Assertions.assertThat(metadata.inputDirectoryPath).isEqualTo(inputDirectory.getAbsolutePath());
  }
}
