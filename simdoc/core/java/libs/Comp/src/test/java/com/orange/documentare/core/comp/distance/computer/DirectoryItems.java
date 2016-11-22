package com.orange.documentare.core.comp.distance.computer;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DirectoryItems {
  private final File directory;
  private final Map<String, Integer> idMap = new HashMap<>();

  private int id;

  TestItem[] getItems() throws IOException {
    File[] files = directory.listFiles();
    TestItem[] testItems = new TestItem[files.length];
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      TestItem testItem = new TestItem(files[i].getName(), FileUtils.readFileToByteArray(file), getIdFor(file));
      testItems[i] = testItem;
    }
    return testItems;
  }

  private int getIdFor(File file) {
    String key = new StringBuilder(file.getAbsolutePath()).reverse().toString();
    if (idMap.containsKey(key)) {
      return idMap.get(key);
    } else {
      ++id;
      idMap.put(key, id);
      return id;
    }
  }
}
