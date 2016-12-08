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
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import lombok.Getter;

@Log4j2
@Getter
public class Page {
  public static final String JSON_ENCODING = "UTF-8";

  private PageRef pageRef;

  public void load(File file) throws IOException {
    try {
      initPageRef(file);
    } catch (IOException e) {
      log.fatal(String.format("FAILED to load page %d", file.toString()), e);
        throw e;
    }
  }

  public void save(File file) throws IOException {
    try {
      savePageRef(file);
    } catch (IOException e) {
      log.fatal(String.format("FAILED to save page %d", file.toString()), e);
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
