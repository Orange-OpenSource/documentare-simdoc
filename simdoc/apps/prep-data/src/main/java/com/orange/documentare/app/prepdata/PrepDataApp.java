package com.orange.documentare.app.prepdata;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.prepdata.cmdline.CommandLineOptions;
import com.orange.documentare.core.image.opencv.OpencvLoader;
import com.orange.documentare.core.prepdata.PrepData;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class PrepDataApp {
  private static final File OUTPUT_DIR = new File("safe-working-dir");
  private static final File METADATA_JSON = new File("metadata.json");
  private static final File BYTESDATA_JSON = new File("bytes-data.json");

  public static void main(String[] args) {
    System.out.println("\n[prep-data - Start]");
    CommandLineOptions options;
    try {
      options = new CommandLineOptions(args);
    } catch (Exception e) {
      CommandLineOptions.showHelp();
      return;
    }
    try {
      doTheJob(options);
      System.out.println("\n[prep-data - Done]");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void doTheJob(CommandLineOptions options) throws IOException {
    if (options.isRawConverterEnabled()) {
      OpencvLoader.load();
    }

    PrepData prepData = PrepData.builder()
            .inputDirectory(options.getInputDir())
            .safeWorkingDirConverter()
            .safeWorkingDirectory(OUTPUT_DIR)
            .preppedBytesDataOutputFile(BYTESDATA_JSON)
            .metadataOutputFile(METADATA_JSON)
            .withRawConverter(options.isRawConverterEnabled())
            .withBytes(options.isWithBytes())
            .expectedRawBytesCount(options.getExpectedBytesSize())
            .build();
    prepData.prep();
  }
}
