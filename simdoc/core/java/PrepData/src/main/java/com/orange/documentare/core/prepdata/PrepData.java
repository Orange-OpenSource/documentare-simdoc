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
import com.orange.documentare.core.model.json.JsonGenericHandler;

import java.io.File;
import java.io.IOException;
import java.io.OptionalDataException;
import java.util.Optional;

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
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    PreppedData preppedData = new PreppedData(bytesData);
    Metadata metadata = new Metadata(inputDirectory.getAbsolutePath());
    try {
      jsonGenericHandler.writeObjectToJsonFile(preppedData, preppedDataOutputFile);
      jsonGenericHandler.writeObjectToJsonFile(metadata, metadataOutputFile);
    } catch (IOException e) {
      throw new IllegalStateException(String.format("Failed to write prepped data to output file '%s': %s", preppedDataOutputFile.getAbsolutePath(), e.getMessage()));
    }
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
      Optional<String> error = Optional.empty();
      if (!inputDirectory.isDirectory()) {
        error = Optional.of("input directory is not a directory...: " + inputDirectory.getAbsolutePath());
      } else if (preppedDataOutputFile == null) {
        error = Optional.of("prepped data output file is null");
      } else if (metadataOutputFile == null) {
        error = Optional.of("metadata output file is null");
      }
      if (error.isPresent()) {
        throw new IllegalStateException(error.get());
      }

      return new PrepData(inputDirectory, preppedDataOutputFile, metadataOutputFile);
    }
  }
}
