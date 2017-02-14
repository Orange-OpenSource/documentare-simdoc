package com.orange.documentare.core.system.filesid;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FilesId {
  private final File sourceDirectory;
  private final File destinationDirectory;

  public static FilesIdBuilder builder() {
    return new FilesIdBuilder();
  }

  public FilesIdMap createFilesIdDirectory() {
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
      Path srcPath = files.get(id).toPath().toAbsolutePath();
      Path destPath = (new File(destinationDirectory.getAbsolutePath() + "/" + id)).toPath().toAbsolutePath();
      try {
        Files.createSymbolicLink(destPath, srcPath);
      } catch (IOException e) {
        throw new FilesIdException(String.format("[FilesId] failed to create symbolic link: %s, %s -> %s", e.getMessage(), srcPath, destPath));
      }
    }
    return files;
  }

  private FilesIdMap buildMap(List<File> srcFiles) {
    FilesIdMap map = new FilesIdMap();
    IntStream.range(0, srcFiles.size())
      .forEach(id -> map.put(id, srcFiles.get(id).getAbsolutePath()));
    return map;
  }

  private void createDestinationDirectory() {
    if (destinationDirectory.exists()) {
      log.info("[FilesId] Directory exists, force delete and recreate: " + destinationDirectory.getAbsolutePath());
    }
    try {
      FileUtils.deleteDirectory(destinationDirectory);
    } catch (IOException e) {
      throw new FilesIdException("[FilesId] failed to recreate(delete) destination directory: " + e.getMessage());
    }
    destinationDirectory.mkdirs();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class FilesIdBuilder {
    private File sourceDirectory;
    private File destinationDirectory;

    public FilesIdBuilder sourceDirectory(File sourceDirectory) {
      this.sourceDirectory = sourceDirectory;
      return this;
    }

    public FilesIdBuilder destinationDirectory(File destinationDirectory) {
      this.destinationDirectory = destinationDirectory;
      return this;
    }

    public FilesId build() {
      Optional<String> error = Optional.empty();
      if (sourceDirectory == null) {
        error = Optional.of("source directory is null");
      } else if (!sourceDirectory.isDirectory()) {
        error = Optional.of("source directory is not a directory: " + sourceDirectory.getAbsolutePath());
      } else if (destinationDirectory == null) {
        error = Optional.of("destination directory is null");
      }
      if (error.isPresent()) {
        throw new FilesIdException("[FilesId] init error: " + error.get());
      }
      return new FilesId(sourceDirectory, destinationDirectory);
    }
  }
}
