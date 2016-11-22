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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileToIdMapper {
  public static final File CSV_FILE = new File("ncd_mapping.csv");

  private int idCount;
  private Map<String, Integer> absoluteFilenameToIdMap = new HashMap<>();
  private List<String> idToAbsoluteFilename = new ArrayList<>();

  int map(File file) {
    int id = idCount++;
    String absoluteFilename = file.getAbsolutePath();
    absoluteFilenameToIdMap.put(absoluteFilename, id);
    idToAbsoluteFilename.add(absoluteFilename);
    return id;
  }

  public int idOf(File file) {
    return absoluteFilenameToIdMap.get(file.getAbsolutePath());
  }

  public void writeMappingCsv() throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(CSV_FILE);
    for (int i = 0; i < idToAbsoluteFilename.size(); i++) {
      writeLine(fileOutputStream, i, idToAbsoluteFilename.get(i));
    }
  }

  private void writeLine(FileOutputStream fileOutputStream, int id, String filename) throws IOException {
    String line = String.format("%10d ; %s\n", id ,filename);
    fileOutputStream.write(line.getBytes());
  }
}
