package com.orange.documentare.core.system.inputfilesconverter;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class InputFilesConverter {
  private final File sourceDirectory;
  private final File destinationDirectory;
  private final FileConverter converter;

  public static FilesIdBuilder builder() {
    return new FilesIdBuilder();
  }

  public FilesMap createFilesIdDirectory() {
    createDestinationDirectory();
    List<File> srcFiles = createFilesId();
    return buildMap(srcFiles);
  }

  private List<File> createFilesId() {
    List<File> files =
      FileUtils.listFiles(sourceDirectory, null, true).stream()
      .filter(file -> !file.isHidden())
      .sorted()
      .collect(Collectors.toList());
    for (int id = 0; id < files.size(); id++) {
      File source = files.get(id);
      File destination = new File(destinationDirectory.getAbsolutePath() + "/" + id);
      converter.convert(source, destination);
    }
    return files;
  }

  private FilesMap buildMap(List<File> srcFiles) {
    FilesMap map = new FilesMap();
    IntStream.range(0, srcFiles.size())
      .forEach(id -> map.put(id, srcFiles.get(id).getAbsolutePath()));
    return map;
  }

  private void createDestinationDirectory() {
    if (destinationDirectory.exists()) {
      log.info("[InputFilesConverter] Directory exists, force delete and recreate: " + destinationDirectory.getAbsolutePath());
    }
    try {
      FileUtils.deleteDirectory(destinationDirectory);
    } catch (IOException e) {
      throw new FileConverterException("[InputFilesConverter] failed to recreate(delete) destination directory: " + e.getMessage());
    }
    destinationDirectory.mkdirs();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class FilesIdBuilder {
    private File sourceDirectory;
    private File destinationDirectory;
    private FileConverter fileConverter;

    public FilesIdBuilder sourceDirectory(File sourceDirectory) {
      this.sourceDirectory = sourceDirectory;
      return this;
    }

    public FilesIdBuilder destinationDirectory(File destinationDirectory) {
      this.destinationDirectory = destinationDirectory;
      return this;
    }

    public FilesIdBuilder fileConverter(FileConverter fileConverter) {
      this.fileConverter = fileConverter;
      return this;
    }

    public InputFilesConverter build() {
      Optional<String> error = Optional.empty();
      if (sourceDirectory == null) {
        error = Optional.of("source directory is null");
      } else if (!sourceDirectory.isDirectory()) {
        error = Optional.of("source directory is not a directory: " + sourceDirectory.getAbsolutePath());
      } else if (destinationDirectory == null) {
        error = Optional.of("destination directory is null");
      } else if (fileConverter == null) {
        error = Optional.of("file converter is null");
      }
      if (error.isPresent()) {
        throw new FileConverterException("[InputFilesConverter] init error: " + error.get());
      }
      return new InputFilesConverter(sourceDirectory, destinationDirectory, fileConverter);
    }
  }
}
