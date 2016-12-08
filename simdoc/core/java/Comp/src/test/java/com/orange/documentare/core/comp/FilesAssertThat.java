package com.orange.documentare.core.comp;
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

import java.io.File;
import java.io.IOException;

public class FilesAssertThat {

  private final Res res;

  public FilesAssertThat(Object obj) {
    res = new Res(obj);
  }

  /**
   * @param referenceResourceName
   * @param outputFilename
   */
  public void theyAreEqual(String outputFilename, String referenceResourceName) throws IOException {
    File referenceFile = res.file(referenceResourceName);
    File outputFile = new File(outputFilename);
    String ref = FileUtils.readFileToString(referenceFile);
    String output = FileUtils.readFileToString(outputFile);
    Assertions.assertThat(output).isEqualTo(ref);
  }
}
