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
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class ThumbnailsBuilder {
  private static final File SOURCE_SYMLINKS_DIR = new File("source-symlinks");
  private static final File THUMBNAILS_DIR = new File("thumbnails");

  private final SymbolicLinkConverter symbolicLinkConverter = new SymbolicLinkConverter();
  private final Metadata metadata;

  /** @return optional image directory absolute path */
  public Optional<String> build() {
    FileUtils.deleteQuietly(SOURCE_SYMLINKS_DIR);
    FileUtils.deleteQuietly(THUMBNAILS_DIR);
    SOURCE_SYMLINKS_DIR.mkdir();
    THUMBNAILS_DIR.mkdir();

    metadata.filesMap.entrySet().parallelStream()
      .forEach(this::buildThumbnail);

    return Optional.of(THUMBNAILS_DIR.getAbsolutePath());
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
      throw new IllegalStateException(String.format("Failed to create thumbnail '%s' from file '%s': %s", sourceImage.getAbsolutePath(), destImage.getAbsolutePath(), e.getMessage()));
    }
  }

  private File buildSymLink(Map.Entry<Integer, String> mapEntry) {
    File source = new File(mapEntry.getValue());
    File destination = new File(SOURCE_SYMLINKS_DIR.getAbsolutePath() + File.separator + mapEntry.getKey());
    symbolicLinkConverter.convert(source, destination);
    return destination;
  }
}
