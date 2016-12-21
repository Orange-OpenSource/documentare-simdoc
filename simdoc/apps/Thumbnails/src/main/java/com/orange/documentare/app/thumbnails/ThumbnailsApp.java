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
import com.orange.documentare.core.system.thumbnail.Thumbnail;

import java.io.IOException;

public class ThumbnailsApp {
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
    Thumbnail thumbnail = new Thumbnail(options.getDirectory());
    thumbnail.createDirectoryFilesThumbnails();
  }
}
