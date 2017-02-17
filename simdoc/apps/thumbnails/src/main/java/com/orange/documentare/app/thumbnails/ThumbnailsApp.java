package com.orange.documentare.app.thumbnails;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.thumbnails.cmdline.CommandLineOptions;
import com.orange.documentare.core.image.opencv.OpencvLoader;
import com.orange.documentare.core.image.thumbnail.Thumbnail;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ThumbnailsApp {
  static {
    OpencvLoader.load();
  }

  private static String OUTPUT_DIR = "thumbnails";

  public static void main(String[] args) {
    System.out.println("\n[Thumbnails - Start]");
    CommandLineOptions options;
    try {
      options = new CommandLineOptions(args);
    } catch(Exception e) {
      CommandLineOptions.showHelp();
      return;
    }
    try {
      doTheJob(options);
      System.out.println("\n[Thumbnails - Done]");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void doTheJob(CommandLineOptions options) throws IOException {
    File outputDir = new File(OUTPUT_DIR);
    FileUtils.deleteQuietly(outputDir);
    outputDir.mkdir();
    for (File file : options.getDirectory().listFiles()) {
      if (Thumbnail.canCreateThumbnail(file)) {
        Thumbnail.createThumbnail(file, new File(OUTPUT_DIR + "/" + file.getName() + ".png"));
      }
    }
  }
}
