package com.orange.documentare.app.ncd;
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
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
public class NcdItem implements DistanceItem {
  /** file name with directory hierarchy below provided root directory */
  private final String relativeFilename;

  /** not used in this app */
  @JsonIgnore
  private final String humanReadableId = null;

  @JsonIgnore
  private byte[] bytes;

  public NcdItem(File file, String rootDirectoryPath) throws IOException {
    relativeFilename = file.getAbsolutePath().replaceFirst(rootDirectoryPath, "");
    bytes = readBytes(file);
  }

  public NcdItem(File file, int id) throws IOException {
    relativeFilename = Integer.toString(id);
    bytes = readBytes(file);
  }

  @Override
  public boolean equals(Object obj) {
    NcdItem otherItem = (NcdItem) obj;
    return relativeFilename.equals(otherItem.relativeFilename);
  }

  private byte[] readBytes(File file) throws IOException {
    return FileUtils.readFileToByteArray(file);
  }
}
