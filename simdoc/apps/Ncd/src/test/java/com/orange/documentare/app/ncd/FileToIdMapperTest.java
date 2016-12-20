package com.orange.documentare.app.ncd;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FileToIdMapperTest {

  @After
  public void cleanup() {
    FileToIdMapper.CSV_FILE.delete();
  }

  @Test
  public void create_a_unique_integer_id_for_an_input_file() {
    // given
    FileToIdMapper fileToIdMapper = new FileToIdMapper();
    File file1 = new File("1");
    File file2 = new File("2");

    // when
    int id1 = fileToIdMapper.map(file1);
    int id2 = fileToIdMapper.map(file2);

    // then
    Assertions.assertThat(id1).isEqualTo(0);
    Assertions.assertThat(id2).isEqualTo(1);
  }

  @Test
  public void maintain_absolute_filename_to_id_map() {
    FileToIdMapper fileToIdMapper = new FileToIdMapper();
    File file1 = new File("1");
    File file2 = new File("2");

    // when
    fileToIdMapper.map(file1);
    fileToIdMapper.map(file2);
    int idForFile1 = fileToIdMapper.idOf(file1);
    int idForFile2 = fileToIdMapper.idOf(file2);


    // then
    Assertions.assertThat(idForFile1).isEqualTo(0);
    Assertions.assertThat(idForFile2).isEqualTo(1);
  }

  @Test
  public void write_mapping_as_csv_file() throws IOException {
    // given
    FileToIdMapper fileToIdMapper = new FileToIdMapper();
    File file1 = new File("dir/file1");
    File file2 = new File("file2");
    fileToIdMapper.map(file1);
    fileToIdMapper.map(file2);

    // when
    fileToIdMapper.writeMappingCsv();
    String csvText = FileUtils.readFileToString(fileToIdMapper.CSV_FILE);

    // then
    Assertions.assertThat(csvText).isEqualTo(String.format("%10d ; %s\n%10d ; %s\n", 0, file1.getAbsolutePath(), 1, file2.getAbsolutePath()));
  }
}
