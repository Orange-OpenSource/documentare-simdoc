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
import com.orange.documentare.core.system.inputfilesconverter.FilesMap;
import com.orange.documentare.core.system.inputfilesconverter.SymbolicLinkConverter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PrepData {
  private final File inputDirectory;
  private final BytesData[] bytesData;
  private final File safeWorkingDirectory;
  private final File preppedBytesDataOutputFile;
  private final File metadataOutputFile;
  private final boolean prepBytesData;
  private final boolean safeWorkingDirConverter;
  private final boolean withRawConverter;
  private final boolean withBytes;

  public static PrepDataBuilder builder() {
    return new PrepDataBuilder();
  }

  public void prep() {
    File inDir = inputDirectory;

    if (safeWorkingDirConverter) {
      inDir = safeWorkingDirectory;
      prepSafeWorkingDirectory();
    }

    if (prepBytesData) {
      prepBytesData(inDir);
    }
  }

  private void prepSafeWorkingDirectory() {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);

    InputFilesConverter inputFilesConverter = InputFilesConverter.builder()
      .sourceDirectory(inputDirectory)
      .bytesData(bytesData)
      .destinationDirectory(safeWorkingDirectory)
      .fileConverter(withRawConverter ? new RawFilesConverter() : new SymbolicLinkConverter())
      .build();
    FilesMap filesMap = inputFilesConverter.createSafeWorkingDirectory();
    Metadata metadata = new Metadata(inputDirectory == null ? "bytes data mode" : inputDirectory.getAbsolutePath(), filesMap, withRawConverter);

    writeJson(metadata, metadataOutputFile, jsonGenericHandler);
  }

  private void prepBytesData(File inDir) {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);

    BytesData[] bytesData = BytesData.loadFromDirectory(inDir, BytesData.relativePathIdProvider(inDir));
    if (withBytes) {
      bytesData = BytesData.withBytes(bytesData);
    }

    PreppedBytesData preppedBytesData = new PreppedBytesData(bytesData);

    writeJson(preppedBytesData, preppedBytesDataOutputFile, jsonGenericHandler);
  }

  private void writeJson(Object object, File outputFile, JsonGenericHandler jsonGenericHandler) {
    try {
      jsonGenericHandler.writeObjectToJsonFile(object, outputFile);
    } catch (IOException e) {
      throw new IllegalStateException(String.format("Failed to write prepped data to output file '%s': %s", outputFile.getAbsolutePath(), e.getMessage()));
    }
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class PrepDataBuilder {
    private File inputDirectory;
    private BytesData[] bytesData;
    private File safeWorkingDirectory;
    private File preppedBytesDataOutputFile;
    private File metadataOutputFile;
    private boolean safeWorkingDirConverter;
    private boolean withRawConverter;
    private boolean withBytes;

    public PrepDataBuilder inputDirectory(File inputDirectory) {
      this.inputDirectory = inputDirectory;
      return this;
    }

    public PrepDataBuilder bytesData(BytesData[] bytesData) {
      this.bytesData = bytesData;
      return this;
    }

    public PrepDataBuilder safeWorkingDirectory(File safeWorkingDirectory) {
      this.safeWorkingDirectory = safeWorkingDirectory;
      return this;
    }

    public PrepDataBuilder preppedBytesDataOutputFile(File preppedBytesDataOutputFile) {
      this.preppedBytesDataOutputFile = preppedBytesDataOutputFile;
      return this;
    }

    public PrepDataBuilder metadataOutputFile(File metadataOutputFile) {
      this.metadataOutputFile = metadataOutputFile;
      return this;
    }

    public PrepDataBuilder safeWorkingDirConverter() {
      safeWorkingDirConverter = true;
      return this;
    }

    public PrepDataBuilder withRawConverter(boolean enable) {
      withRawConverter = enable;
      return this;
    }

    public PrepDataBuilder withBytes(boolean enable) {
      withBytes = enable;
      return this;
    }

    public PrepData build() {
      boolean prepBytesData = preppedBytesDataOutputFile != null;

      Optional<String> error = Optional.empty();
      if (inputDirectory == null && bytesData == null) {
        error = Optional.of("input directory file and bytes data are null");
      } else if ((bytesData == null) && !inputDirectory.isDirectory()) {
        error = Optional.of("input directory is not a directory...: " + inputDirectory.getAbsolutePath());
      } else if (safeWorkingDirConverter && safeWorkingDirectory == null) {
        error = Optional.of("safe working directory is null");
      } else if (safeWorkingDirConverter && metadataOutputFile == null) {
        error = Optional.of("metadata output file is null");
      } else if (!safeWorkingDirConverter && !withRawConverter && !prepBytesData) {
        error = Optional.of("no converter");
      }
      if (error.isPresent()) {
        throw new IllegalStateException(error.get());
      }
      return new PrepData(inputDirectory, bytesData, safeWorkingDirectory, preppedBytesDataOutputFile, metadataOutputFile, prepBytesData, safeWorkingDirConverter, withRawConverter, withBytes);
    }
  }
}
