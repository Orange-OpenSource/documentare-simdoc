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
import java.util.stream.IntStream;

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
    this(id, filepath, null, true);
  }

  public BytesData(String id, byte[] bytes) {
    this(id, null, bytes, false);
  }

  // required by jackson to deserialize the object
  public BytesData() {
    this(null, null, null, false);
  }

  /**
   * In case some elements of the provided array just have a filepath entry (bytes == null),
   * this function will return a new array in which bytes arrays are loaded thanks to filepath entries.
   * It can be useful when we built BytesData only with the filepath and no bytes
   * @param bytesData
   * @return new BytesData array in which bytes are present
   */
  public static BytesData[] withBytes(BytesData[] bytesData) {
    return Arrays.stream(bytesData)
      .map(b -> new BytesData(b.id, b.filepath, b.bytes, true))
      .toArray(size -> new BytesData[size]);
  }

  public static BytesData withBytes(BytesData b) {
    return new BytesData(b.id, b.filepath, b.bytes, true);
  }

  public static BytesData loadWithoutBytes(String id, String filepath) {
    return new BytesData(id, filepath, null, false);
  }

  public static BytesData[] loadFromDirectory(File directory, FileIdProvider fileIdProvider) {
    return loadFromDirectory(directory, fileIdProvider, true);
  }

  public static BytesData[] loadFromDirectory(File directory) {
    return loadFromDirectory(directory, null, true);
  }

  public static BytesData[] buildFromDirectoryWithoutBytes(File directory) {
    return loadFromDirectory(directory, null, false);
  }

  public static BytesData[] buildFromDirectoryWithoutBytes(File directory, FileIdProvider fileIdProvider) {
    return loadFromDirectory(directory, fileIdProvider, false);
  }

  private static BytesData[] loadFromDirectory(File directory, FileIdProvider fileIdProvider, boolean withBytes) {
    if (!directory.isDirectory()) {
      throw new IllegalStateException(String.format("Failed to load data from invalid directory '%s': not a directory", directory.getAbsolutePath()));
    }

    FileIdProvider idProvider = fileIdProvider == null ? file -> file.getAbsolutePath() : fileIdProvider;
    return FileUtils.listFiles(directory, null, true).stream()
      .filter(file -> !file.isHidden())
      .sorted() // For the sake of tests: it is mandatory to keep same order across different test platform...
      .map(file -> new BytesData(idProvider.idFor(file), file.getAbsolutePath(), null, withBytes))
      .toArray(size -> new BytesData[size]);
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

  private BytesData(String id, String filepath, byte[] bytes, boolean loadBytes) {
    this.id = id;
    this.filepath = filepath;
    this.bytes = loadBytes ? loadBytesFromFile(filepath) : bytes;
  }

  private byte[] loadBytesFromFile(String filepath) {
    try {
      return FileUtils.readFileToByteArray(new File(filepath));
    } catch (IOException e) {
      throw new IllegalStateException(String.format("Failed to load file '%s': '%s'", filepath, e.getMessage()));
    }
  }
}
