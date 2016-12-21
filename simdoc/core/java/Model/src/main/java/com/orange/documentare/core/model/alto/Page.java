package com.orange.documentare.core.model.alto;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.alto.ref.page.PageRef;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Slf4j
@Getter
public class Page {
  public static final String JSON_ENCODING = "UTF-8";

  private PageRef pageRef;

  public void load(File file) throws IOException {
    try {
      initPageRef(file);
    } catch (IOException e) {
      log.error(String.format("FAILED to load page %d", file.toString()), e);
      throw e;
    }
  }

  public void save(File file) throws IOException {
    try {
      savePageRef(file);
    } catch (IOException e) {
      log.error(String.format("FAILED to save page %d", file.toString()), e);
      throw e;
    }
  }

  private void initPageRef(File file) throws IOException {
    String jsonString = FileUtils.readFileToString(file, JSON_ENCODING);
    pageRef = JsonHelper.getPageRef(jsonString);
  }

  private void savePageRef(File file) throws IOException {
    String jsonString = JsonHelper.getJson(pageRef, true);
    FileUtils.writeStringToFile(file, jsonString, JSON_ENCODING);
  }
}
