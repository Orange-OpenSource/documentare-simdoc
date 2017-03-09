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
import com.orange.documentare.core.system.thumbnail.Thumbnail;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class ThumbnailsBuilder {

  private static final File THUMBNAILS_DIR = new File("thumbnails");

  private final Metadata metadata;

  /** @return optional image directory absolute path */
  public Optional<String> build() {
    FileUtils.deleteQuietly(THUMBNAILS_DIR);
    THUMBNAILS_DIR.mkdir();

    metadata.filesMap.entrySet().stream()
      .forEach(this::buildThumbnail);

    return Optional.of(THUMBNAILS_DIR.getAbsolutePath());
  }

  private void buildThumbnail(Map.Entry<Integer, String> mapEntry) {
    File sourceImage = new File(mapEntry.getValue());
    File destImage = new File(THUMBNAILS_DIR.getAbsolutePath() + "/" + mapEntry.getKey() + ".png");
    try {
      if (Thumbnail.canCreateThumbnail(sourceImage)) {
        Thumbnail.createThumbnail(sourceImage, destImage);
      }
    } catch (IOException e) {
      throw new IllegalStateException(String.format("Failed to create thumbnail '%s' form file '%s': %s", sourceImage.getAbsolutePath(), destImage.getAbsolutePath(), e.getMessage()));
    }
  }
}
