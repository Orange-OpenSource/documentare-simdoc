package com.orange.documentare.app.base64;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.base64.cmdline.CommandLineOptions;
import com.orange.documentare.core.image.opencv.OpencvLoader;
import com.orange.documentare.core.prepdata.PrepData;
import com.orange.documentare.core.prepdata.RawFilesConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Base64App {
  private static final File OUTPUT_DIR = new File("base64");
  private static final File RAW_DIR = new File("raw");
  private static final RawFilesConverter rawFilesConverter = new RawFilesConverter(0);
  private static CommandLineOptions options;

  public static void main(String[] args) {
    System.out.println("\n[base64 - Start]");
    try {
      options = new CommandLineOptions(args);
    } catch (Exception e) {
      CommandLineOptions.showHelp();
      return;
    }
    try {
      doTheJob(options);
      System.out.println("\n[base64 - Done]");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void doTheJob(CommandLineOptions options) throws IOException {
    FileUtils.deleteQuietly(OUTPUT_DIR);
    FileUtils.deleteQuietly(RAW_DIR);
    OUTPUT_DIR.mkdir();

    if (options.isRawConverterEnabled()) {
      OpencvLoader.load();
      RAW_DIR.mkdir();
    }

    FileUtils.listFiles(options.getInputDir(), null, true).stream()
        .filter(file -> !file.isHidden())
        .forEach(Base64App::convert);
  }

  private static void convert(File file) {
    File src = file;
    if (options.isRawConverterEnabled() && rawFilesConverter.isImage(file)) {
      File rawDest = new File(RAW_DIR.getAbsolutePath() + "/" + file.getName());
      rawFilesConverter.convert(file, rawDest);
      src = rawDest;
    }
    File dest = new File(OUTPUT_DIR.getAbsolutePath() + "/" + file.getName());
    try {
      Base64Converter.convert(src, dest);
    } catch (IOException e) {
      System.out.println(String.format("Failed to encode file %s to %s: %s", src.getAbsolutePath(), dest.getAbsolutePath(), e.getMessage()));
    }
  }
}
