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

import com.orange.documentare.core.image.opencv.OpencvLoader;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.system.inputfilesconverter.FilesMap;
import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.CRC32;

public class PrepDataTest {
  private static File PREPPED_DATA_JSON = new File("bytes-data.json");
  private static File METADATA_JSON = new File("metadata.json");
  private static File SAFE_WORKING_DIRECTORY = new File("safe-working-directory");

  static {
    OpencvLoader.load();
  }

  @After
  public void cleanup() {
    FileUtils.deleteQuietly(PREPPED_DATA_JSON);
    FileUtils.deleteQuietly(METADATA_JSON);
    FileUtils.deleteQuietly(SAFE_WORKING_DIRECTORY);
  }

  @Test(expected = IllegalStateException.class)
  public void raise_exception_if_input_directory_is_null() {
    PrepData.builder()
      .inputDirectory(null)
      .metadataOutputFile(METADATA_JSON)
      .build();
  }

  @Test(expected = IllegalStateException.class)
  public void raise_exception_if_input_directory_does_not_exists() {
    PrepData.builder()
      .inputDirectory(new File("/pouet-pouet"))
      .metadataOutputFile(METADATA_JSON)
      .build();
  }

  @Test(expected = IllegalStateException.class)
  public void raise_exception_if_safe_working_dir_but_dir_is_null() {
    File inputDirectory = new File(getClass().getResource("/prep-data-test-input-dir").getFile());
    PrepData.builder()
      .inputDirectory(inputDirectory)
      .safeWorkingDirConverter()
      .safeWorkingDirectory(null)
      .metadataOutputFile(METADATA_JSON)
      .build();
  }

  @Test(expected = IllegalStateException.class)
  public void raise_exception_if_safe_working_dir_but_metadata_file_is_null() {
    File inputDirectory = new File(getClass().getResource("/prep-data-test-input-dir").getFile());
    PrepData.builder()
      .inputDirectory(inputDirectory)
      .safeWorkingDirConverter()
      .safeWorkingDirectory(SAFE_WORKING_DIRECTORY)
      .metadataOutputFile(null)
      .build();
  }

  @Test(expected = IllegalStateException.class)
  public void raise_exception_if_no_converter() {
    File inputDirectory = new File(getClass().getResource("/prep-data-test-input-dir").getFile());
    PrepData.builder()
      .inputDirectory(inputDirectory)
      .metadataOutputFile(METADATA_JSON)
      .build();
  }

  @Test
  public void prep_bytes_data() throws IOException {
    // Given
    File inputDirectory = new File(getClass().getResource("/prep-data-test-input-dir").getFile());
    PrepData prepData = PrepData.builder()
      .inputDirectory(inputDirectory)
      .preppedBytesDataOutputFile(PREPPED_DATA_JSON)
      .build();

    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
    // When
    prepData.prep();
    PreppedBytesData preppedBytesData =
      (PreppedBytesData) jsonGenericHandler.getObjectFromJsonFile(PreppedBytesData.class, PREPPED_DATA_JSON);

    // Then
    Assertions.assertThat(preppedBytesData.bytesData.length).isEqualTo(2);
    Assertions.assertThat(preppedBytesData.bytesData[0].id).isEqualTo("image.png");
    Assertions.assertThat(preppedBytesData.bytesData[1].id).isEqualTo("subdir/opossum");
  }

  @Test
  public void prep_safe_working_directory() throws IOException {
    // Given
    String safeWdPath = SAFE_WORKING_DIRECTORY.getAbsolutePath();
    File inputDirectory = new File(getClass().getResource("/prep-data-test-input-dir").getFile());
    PrepData prepData = PrepData.builder()
      .inputDirectory(inputDirectory)
      .safeWorkingDirConverter()
      .safeWorkingDirectory(SAFE_WORKING_DIRECTORY)
      .metadataOutputFile(METADATA_JSON)
      .build();

    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();

    // When
    prepData.prep();
    File[] safeFiles = SAFE_WORKING_DIRECTORY.listFiles();
    Metadata metadata =
      (Metadata) jsonGenericHandler.getObjectFromJsonFile(Metadata.class, METADATA_JSON);
    FilesMap filesMap = metadata.filesMap;

    // Then
    Assertions.assertThat(safeFiles.length).isEqualTo(2);
    Assertions.assertThat(safeFiles[0].getAbsolutePath()).isEqualTo(new File(safeWdPath + "/0").getAbsolutePath());
    Assertions.assertThat(safeFiles[1].getAbsolutePath()).isEqualTo(new File(safeWdPath + "/1").getAbsolutePath());
    Assertions.assertThat(Files.isSymbolicLink(safeFiles[0].toPath())).isTrue();
    Assertions.assertThat(Files.isSymbolicLink(safeFiles[1].toPath())).isTrue();
    Assertions.assertThat(metadata.inputDirectoryPath).isEqualTo(inputDirectory.getAbsolutePath());
    Assertions.assertThat(filesMap.size()).isEqualTo(2);
    Assertions.assertThat(new File(filesMap.get(0)).exists()).isTrue();
    Assertions.assertThat(new File(filesMap.get(1)).exists()).isTrue();
  }

  @Test
  public void prep_safe_working_directory_with_raw() throws IOException {
    // Given
    File inputDirectory = new File(getClass().getResource("/prep-data-test-input-dir").getFile());
    PrepData prepData = PrepData.builder()
      .inputDirectory(inputDirectory)
      .safeWorkingDirConverter()
      .safeWorkingDirectory(SAFE_WORKING_DIRECTORY)
      .rawConverter()
      .metadataOutputFile(METADATA_JSON)
      .build();

    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();

    // When
    prepData.prep();
    File[] safeFiles = SAFE_WORKING_DIRECTORY.listFiles();
    Metadata metadata =
      (Metadata) jsonGenericHandler.getObjectFromJsonFile(Metadata.class, METADATA_JSON);
    FilesMap filesMap = metadata.filesMap;

    byte[] rawBytes = FileUtils.readFileToByteArray(safeFiles[0]);
    CRC32 crc32 = new CRC32();
    crc32.update(rawBytes);

    // Then
    Assertions.assertThat(safeFiles.length).isEqualTo(2);
    Assertions.assertThat(Files.isSymbolicLink(safeFiles[0].toPath())).isFalse();
    Assertions.assertThat(Files.isSymbolicLink(safeFiles[1].toPath())).isTrue();
    Assertions.assertThat(filesMap.size()).isEqualTo(2);
    Assertions.assertThat(crc32.getValue()).isEqualTo(2829745108L);
  }
}
