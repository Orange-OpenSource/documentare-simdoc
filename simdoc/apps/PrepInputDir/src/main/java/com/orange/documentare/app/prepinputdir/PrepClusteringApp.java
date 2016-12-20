package com.orange.documentare.app.prepinputdir;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.prepinputdir.cmdline.CommandLineOptions;
import com.orange.documentare.core.filesio.filesid.FilesIdBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class PrepClusteringApp {

  private static final File OUTPUT_DIR = new File("safe-input-dir");

  public static void main(String[] args) {
    CommandLineOptions options;
    try {
      options = new CommandLineOptions(args);
    } catch (Exception e) {
      CommandLineOptions.showHelp();
      return;
    }
    try {
      doTheJob(options);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void doTheJob(CommandLineOptions options) throws IOException {
    FilesIdBuilder builder = new FilesIdBuilder();
    builder.createFilesIdDirectory(options.getInputDir().getAbsolutePath(), OUTPUT_DIR.getAbsolutePath(), "./");
    log.info("\n[Done]");
  }
}
