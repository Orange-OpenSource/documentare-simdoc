package com.orange.documentare.core.comp.distance.bytesdistances;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Denis Boisset & Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import lombok.EqualsAndHashCode;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
public final class BytesData implements DistanceItem {

  public interface FileIdProvider {
    String idFor(File file);
  }

  public final String id;
  public final String filepath;

  public final byte[] bytes;

  @Override
  @JsonIgnore
  public String getHumanReadableId() {
    return id;
  }

  @Override
  public byte[] getBytes() {
    return bytes;
  }

  public BytesData(String id, String filepath) {
    this.id = id;
    this.filepath = filepath;
    this.bytes = loadBytesFromFile(filepath);
  }

  public BytesData(String id, byte[] bytes) {
    this.id = id;
    this.filepath = null;
    this.bytes = bytes;
  }


  public static BytesData[] loadFromDirectory(File directory, FileIdProvider fileIdProvider) {
    if (!directory.isDirectory()) {
      throw new IllegalStateException(String.format("Failed to load data from invalid directory '%s': not a directory", directory.getAbsolutePath()));
    }

    FileIdProvider idProvider = fileIdProvider == null ? file -> file.getAbsolutePath() : fileIdProvider;
    return Arrays.stream(directory.listFiles())
      .filter(file -> !file.isHidden())
      .sorted() // For the sake of tests: it is mandatory to keep same order across different test platform...
      .map(file -> new BytesData(idProvider.idFor(file), file.getAbsolutePath()))
      .toArray(size -> new BytesData[size]);
  }

  public static BytesData[] loadFromDirectory(File directory) {
    return loadFromDirectory(directory, null);
  }

  public static FileIdProvider relativePathIdProvider(File rootDirectory) {
    return file -> {
      String filepath = file.getAbsolutePath();
      String relativeFileName = filepath.replace(rootDirectory.getAbsolutePath(), "");
      if (relativeFileName.startsWith(File.separator)) {
        relativeFileName = relativeFileName.substring(1);
      }
      return relativeFileName;
    };
  }

  // required by jackson to deserialize the object
  public BytesData() {
    this.id = null;
    this.filepath = null;
    this.bytes = null;
  }

  private byte[] loadBytesFromFile(String filepath) {
    try {
      return FileUtils.readFileToByteArray(new File(filepath));
    } catch (IOException e) {
      throw new IllegalStateException(String.format("Failed to load file '%s': '%s'", filepath, e.getMessage()));
    }
  }
}
