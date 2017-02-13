package com.orange.documentare.core.comp.distance.bytesdistances;
/*
 * Copyright (c) 2016 Orange
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

public class BytesDataTest {

  private static final String FILE_PATH = "titi";
  private static final String FILE_CONTENT = "tata";
  private static final String JSON_FILE_PATH = "fileBytesData.json";
  private static final String BYTES_DATA_JSON = "byteData.json";

  private static final String DIR_1 = "/bestioles";


  private JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);

  @After
  public void cleanUp() {
    FileUtils.deleteQuietly(new File(JSON_FILE_PATH));
    FileUtils.deleteQuietly(new File(BYTES_DATA_JSON));
  }

  @Test
  public void write_file_data_as_json_and_reload_it_from_json() throws IOException {
    // given
    FileUtils.writeStringToFile(new File(FILE_PATH), FILE_CONTENT);
    String id1 = "id1";
    BytesData bytesDataFile = new BytesData(id1, FILE_PATH);

    // when
    jsonGenericHandler.writeObjectToJsonFile(bytesDataFile, new File(JSON_FILE_PATH));
    bytesDataFile = (BytesData) jsonGenericHandler.getObjectFromJsonFile(BytesData.class, new File(JSON_FILE_PATH));

    // then
    Assertions.assertThat(bytesDataFile.id).isEqualTo(id1);
    Assertions.assertThat(bytesDataFile.filepath).isEqualTo(FILE_PATH);
    Assertions.assertThat(bytesDataFile.bytes).isEqualTo(FILE_CONTENT.getBytes());

  }

  @Test
  public void write_bytes_data_as_json_and_reload_it_from_json() throws IOException {
    // given
    byte[] bytes = new byte[1];
    bytes[0] = 12;
    String id2 = "id2";
    BytesData bytesDataByte = new BytesData(id2, bytes);

    // when
    jsonGenericHandler.writeObjectToJsonFile(bytesDataByte, new File(BYTES_DATA_JSON));
    BytesData actualBytesDataByte = (BytesData) jsonGenericHandler.getObjectFromJsonFile(BytesData.class, new File(BYTES_DATA_JSON));

    // then
    Assertions.assertThat(bytesDataByte.id).isEqualTo(id2);
    Assertions.assertThat(actualBytesDataByte.bytes).isEqualTo(bytesDataByte.bytes);
  }

  @Test(expected=IllegalStateException.class)
  public void exception_is_raised_when_file_can_not_be_read() {
    // given
    String id1 = "id3";
    // when
    new BytesData(id1, "/pouet-pouet");
    // then
  }

  @Test
  public void build_data_array_from_directory_content() {
    // Given
    File directory = new File(getClass().getResource(DIR_1).getFile());

    // When
    BytesData[] bytesDataArray = BytesData.loadFromDirectory(directory);

    // Then
    Assertions.assertThat(bytesDataArray).hasSize(34);
  }
}
