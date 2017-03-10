package com.orange.documentare.app.graph;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.prepdata.Metadata;
import com.orange.documentare.core.system.inputfilesconverter.FilesMap;
import com.orange.documentare.core.system.inputfilesconverter.SymbolicLinkConverter;
import com.orange.documentare.core.system.thumbnail.Thumbnail;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class ThumbnailsBuilder {
  private static final File SOURCE_SYMLINKS_DIR = new File("source-symlinks");
  private static final File THUMBNAILS_DIR = new File("thumbnails");

  private final SymbolicLinkConverter symbolicLinkConverter = new SymbolicLinkConverter();
  private final Metadata metadata;
  private final Optional<File> thumbnailsSourceDirectory;
  private final String inputDirectoryPath;

  /** @return optional image directory absolute path */
  public Optional<File> build() {
    FileUtils.deleteQuietly(SOURCE_SYMLINKS_DIR);
    FileUtils.deleteQuietly(THUMBNAILS_DIR);
    SOURCE_SYMLINKS_DIR.mkdir();
    THUMBNAILS_DIR.mkdir();

    metadata.filesMap.entrySet().parallelStream()
      .forEach(this::buildThumbnail);

    return Optional.of(THUMBNAILS_DIR);
  }

  private void buildThumbnail(Map.Entry<Integer, String> mapEntry) {
    File sourceImage = buildSymLink(mapEntry);
    File destImage = new File(THUMBNAILS_DIR.getAbsolutePath() + "/" + mapEntry.getKey() + ".png");
    String label = FilesMap.simpleFilename(mapEntry.getValue());
    try {
      if (Thumbnail.canCreateThumbnail(sourceImage)) {
        Thumbnail.createThumbnail(sourceImage, destImage, label);
      }
    } catch (IOException e) {
      throw new IllegalStateException(String.format("Failed to create thumbnail '%s' from file '%s': %s", sourceImage.getAbsolutePath(), destImage.getAbsolutePath()));
    }
  }

  private File buildSymLink(Map.Entry<Integer, String> mapEntry) {
    String sourceAbsPath = buildSourceAbsPath(mapEntry.getValue());
    File source = new File(sourceAbsPath);
    File destination = new File(SOURCE_SYMLINKS_DIR.getAbsolutePath() + File.separator + mapEntry.getKey());
    symbolicLinkConverter.convert(source, destination);
    return destination;
  }

  private String buildSourceAbsPath(String filePath) {
    return thumbnailsSourceDirectory.isPresent() ? buildPathFromThumbnailSourceDir(filePath) : filePath;
  }

  private String buildPathFromThumbnailSourceDir(String filePath) {
    String sourceAbsPath;
    String relativePath = filePath.replace(inputDirectoryPath, "");
    if (relativePath.contains(".")) {
      int extensionIndex = relativePath.lastIndexOf(".");
      relativePath = relativePath.substring(0, extensionIndex);
    }
    Collection<File> files = FileUtils.listFiles(thumbnailsSourceDirectory.get(), null, true);
    final String searchPath = relativePath;
    Optional<File> sourceFile = files.stream()
      .filter(file -> file.getAbsolutePath().contains(searchPath))
      .findFirst();
    if (!sourceFile.isPresent()) {
      throw new IllegalStateException(String.format("Failed to find thumbnail source file in directory '%s', for file %s", thumbnailsSourceDirectory.get().getAbsolutePath(), filePath));
    }
    sourceAbsPath = sourceFile.get().getAbsolutePath();
    return sourceAbsPath;
  }
}
