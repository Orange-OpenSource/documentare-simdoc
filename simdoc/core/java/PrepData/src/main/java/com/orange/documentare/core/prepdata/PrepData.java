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
import com.orange.documentare.core.system.inputfilesconverter.InputFilesConverter;
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
  private final File idDestinationDirectory;
  private final File preppedBytesDataOutputFile;
  private final File metadataOutputFile;
  private final boolean prepBytesData;
  private final boolean safeWorkingDirConverter;
  private final boolean rawConverter;

  public static PrepDataBuilder builder() {
    return new PrepDataBuilder();
  }

  public void prep() {
    File inDir = inputDirectory;

    if (safeWorkingDirConverter) {
      inDir = idDestinationDirectory;
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
      .destinationDirectory(idDestinationDirectory)
      .fileConverter(rawConverter ? new RawFilesConverter() : new SymbolicLinkConverter())
      .build();
    FilesMap filesMap = inputFilesConverter.createSafeWorkingDirectory();
    Metadata metadata = new Metadata(inputDirectory.getAbsolutePath(), filesMap);

    writeJson(metadata, metadataOutputFile, jsonGenericHandler);
  }

  private void prepBytesData(File inDir) {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);

    BytesData[] bytesData =
      BytesData.buildFromDirectoryWithoutBytes(inDir, BytesData.relativePathIdProvider(inDir));
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
    private File idDestinationDirectory;
    private File preppedBytesDataOutputFile;
    private File metadataOutputFile;
    private boolean safeWorkingDirConverter;
    private boolean rawConverter;

    public PrepDataBuilder inputDirectory(File inputDirectory) {
      this.inputDirectory = inputDirectory;
      return this;
    }

    public PrepDataBuilder idDestinationDirectory(File idDestinationDirectory) {
      this.idDestinationDirectory = idDestinationDirectory;
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

    public PrepDataBuilder rawConverter() {
      rawConverter = true;
      return this;
    }

    public PrepData build() {
      Optional<String> error = Optional.empty();
      if (!inputDirectory.isDirectory()) {
        error = Optional.of("input directory is not a directory...: " + inputDirectory.getAbsolutePath());
      } else if (safeWorkingDirConverter && idDestinationDirectory == null) {
        error = Optional.of("id destination directory is null");
      } else if (metadataOutputFile == null) {
        error = Optional.of("metadata output file is null");
      }
      if (error.isPresent()) {
        throw new IllegalStateException(error.get());
      }
      boolean prepBytesData = preppedBytesDataOutputFile != null;
      return new PrepData(inputDirectory, idDestinationDirectory, preppedBytesDataOutputFile, metadataOutputFile, prepBytesData, safeWorkingDirConverter, rawConverter);
    }
  }
}
