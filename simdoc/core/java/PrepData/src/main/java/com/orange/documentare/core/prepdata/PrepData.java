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


import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;

import java.io.File;

public class PrepData {
  public final File inputDirectory;
  public final File preppedDataOutputFile;
  public final File metadataOutputFile;

  private PrepData(File inputDirectory, File preppedDataOutputFile, File metadataOutputFile) {
    this.inputDirectory = inputDirectory;
    this.preppedDataOutputFile = preppedDataOutputFile;
    this.metadataOutputFile = metadataOutputFile;
  }

  public void prep() {
    BytesData[] bytesData =
      BytesData.buildFromDirectoryWithoutBytes(inputDirectory, BytesData.relativePathIdProvider(inputDirectory));

  }

  public static PrepDataBuilder builder() {
    return new PrepDataBuilder();
  }

  public static class PrepDataBuilder {
    private File inputDirectory;
    private File preppedDataOutputFile;
    private File metadataOutputFile;

    public PrepDataBuilder inputDirectory(File inputDirectory) {
      this.inputDirectory = inputDirectory;
      return this;
    }

    public PrepDataBuilder preppedDataOutputFile(File preppedDataOutputFile) {
      this.preppedDataOutputFile = preppedDataOutputFile;
      return this;
    }

    public PrepDataBuilder metadataOutputFile(File metadataOutputFile) {
      this.metadataOutputFile = metadataOutputFile;
      return this;
    }

    public PrepData build() {
      return new PrepData(inputDirectory, preppedDataOutputFile, metadataOutputFile);
    }
  }
}
