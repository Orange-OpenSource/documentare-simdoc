package com.orange.documentare.core.prepdata;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */


import com.orange.documentare.core.system.inputfilesconverter.FilesMap;
import org.fest.assertions.Assertions;
import org.junit.Test;

public class InputFilesConverterMapTest {

  @Test
  public void extract_complete_name_if_path_separator_is_not_found() {
    // Given
    String expected = "christmas-carol";
    FilesMap map = new FilesMap();
    map.put(0, expected);

    // When
    String simpleFilename = map.simpleFilenameAt(0);

    // Then
    Assertions.assertThat(simpleFilename).isEqualTo(expected);
  }

  @Test
  public void extract_simple_filename_when_directories_path_are_present() {
    // Given
    String expected = "christmas-carol";
    String path = "/Morten/Lauridsen/Kings/College/" + expected;
    FilesMap map = new FilesMap();
    map.put(0, path);

    // When
    String simpleFilename = map.simpleFilenameAt(0);

    // Then
    Assertions.assertThat(simpleFilename).isEqualTo(expected);
  }
}
