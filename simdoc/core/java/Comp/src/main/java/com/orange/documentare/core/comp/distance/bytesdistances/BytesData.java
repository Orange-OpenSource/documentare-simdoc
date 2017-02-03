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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BytesData implements DistanceItem {
  public final String id;
  public final String filepath;
  public final byte[] bytes;

  @Override
  @JsonIgnore
  public String getHumanReadableId() {
    return "id: " + id + ", filepath: " + filepath;
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
