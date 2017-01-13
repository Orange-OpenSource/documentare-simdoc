package com.orange.documentare.core.comp.distance.filesdistances;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public class FileDistanceItem implements DistanceItem {
  /** file name with directory hierarchy under provided root directory */
  public final String relativeFilename;

  /** transient bytes array, released after the distance computation */
  @JsonIgnore
  private byte[] bytes;

  FileDistanceItem(File file, String rootDirectoryPath) throws IOException {
    relativeFilename = retrieveRelativeFilename(file, rootDirectoryPath);
    bytes = readBytes(file);
  }

  /**
   * cunning trick, use file path to detect equality, to speed up NCD
   * NB: if files are equal but duplicated with distinct file names, NCD will have to
   *     compute the distance anyway...
   */
  @Override
  public boolean equals(Object obj) {
    FileDistanceItem otherItem = (FileDistanceItem) obj;
    return relativeFilename.equals(otherItem.relativeFilename);
  }

  private String retrieveRelativeFilename(File file, String rootDirectoryPath) {
    String relativeFileName = file.getAbsolutePath().replace(rootDirectoryPath, "");
    if (relativeFileName.startsWith(File.separator)) {
      relativeFileName = relativeFileName.substring(1);
    }
    return relativeFileName;
  }

  private byte[] readBytes(File file) throws IOException {
    // FIXME: using direct buffer mapped on File could avoid keeping all data in memory...
    return FileUtils.readFileToByteArray(file);
  }

  void releaseBytes() {
    bytes = null;
  }

  @Override
  @JsonIgnore
  public String getHumanReadableId() {
    return relativeFilename;
  }

  @Override
  public byte[] getBytes() {
    return bytes;
  }
}
