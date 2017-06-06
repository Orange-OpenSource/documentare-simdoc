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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import lombok.EqualsAndHashCode;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@EqualsAndHashCode
public final class BytesData implements DistanceItem {

  public interface FileIdProvider {
    String idFor(File file);
  }

  private static final LoadingCache<File, byte[]> fileCache = CacheBuilder.newBuilder()
    .softValues()
    .recordStats()
    .build(new CacheLoader<File, byte[]>() {
      @Override
      public byte[] load(File file) throws Exception {
        return FileUtils.readFileToByteArray(file);
      }
    });

  public final String id;
  public final String filepath;
  public final byte[] bytes;

  @Override
  public String getHumanReadableId() {
    return id;
  }

  @Override
  // in file mode, bytes should always be null and we should rely on the file cache to retrieve dat
  // => indeed, we rely on the cache to free file data under memory pressure
  public byte[] getBytes() {
    return bytes != null ? bytes : loadBytesFromFile(filepath);
  }

  public BytesData(String id, String filepath) {
    this(id, filepath, null);
    if (!(new File(filepath)).isFile()) {
      throw new IllegalStateException("File is not a file: " + filepath);
    }
  }

  public BytesData(String id, byte[] bytes) {
    this(id, null, bytes);
  }

  // required by jackson to deserialize the object
  public BytesData() {
    this(null, null, null);
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
      .map(BytesData::withBytes)
      .toArray(size -> new BytesData[size]);
  }

  public static BytesData withBytes(BytesData b) {
    return new BytesData(b.id, b.filepath, loadBytesFromFile(b.filepath));
  }

  public static BytesData[] loadFromDirectory(File directory) {
    return loadFromDirectory(directory, null);
  }

  public static BytesData[] loadFromDirectory(File directory, FileIdProvider fileIdProvider) {
    if (!directory.isDirectory()) {
      throw new IllegalStateException(String.format("Failed to load data from invalid directory '%s': not a directory", directory.getAbsolutePath()));
    }

    FileIdProvider idProvider = fileIdProvider == null ? file -> file.getAbsolutePath() : fileIdProvider;
    return FileUtils.listFiles(directory, null, true).stream()
      .filter(file -> !file.isHidden())
      .sorted() // For the sake of tests: it is mandatory to keep same order across different test platform...
      .map(file -> new BytesData(idProvider.idFor(file), file.getAbsolutePath(), null))
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

  public static String cacheStats() {
    return fileCache.stats() + " - size = " + fileCache.size();
  }

  private BytesData(String id, String filepath, byte[] bytes) {
    this.id = id;
    this.filepath = filepath;
    this.bytes = bytes;
  }

  private static byte[] loadBytesFromFile(String filepath) {
    File file = new File(filepath);
    if (!file.isFile()) {
      throw new IllegalStateException("Not a file: " + file.getAbsolutePath());
    }
    // FIXME cache is disabled
    try {
      return FileUtils.readFileToByteArray(file);
    } catch (IOException e) {
      throw new IllegalStateException("Not a file: " + file.getAbsolutePath());
    }
    //return fileCache.getUnchecked(file);
  }
}
