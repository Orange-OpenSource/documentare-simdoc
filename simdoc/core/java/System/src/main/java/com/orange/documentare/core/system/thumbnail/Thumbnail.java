package com.orange.documentare.core.system.thumbnail;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.system.nativeinterface.NativeInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Thumbnail {
  private static final String[] SUPPORT_THUMBNAILS = {
    ".png", ".jpg", ".jpeg", ".tif", ".tiff", ".bmp", ".pdf"
  };

  private static final String CONVERT_CMD = "convert";

  // NB: widthxheight Maximum values of height and width given, aspect ratio preserved
  private static final String[] CONVERT_OPTIONS = {
    "-thumbnail", "200x200"
  };

  public static boolean canCreateThumbnail(File file) throws IOException {
    File target;
    target = Files.isSymbolicLink(file.toPath()) ?
      Files.readSymbolicLink(file.toPath()).toFile() :
      file;

    String filename = target.getName().toLowerCase();
    return Arrays.asList(SUPPORT_THUMBNAILS).stream()
      .filter(extension -> filename.endsWith(extension))
      .count() > 0;
  }

  public static void createThumbnail(File image, File thumbnail) throws IOException {
    if (image == null || thumbnail == null) {
      throw new NullPointerException(String.format("Can not create thumbnail, provided image '%s' or thumbnail '%s' file is null", image, thumbnail));
    }
    List<String> options = new ArrayList<>(Arrays.asList(CONVERT_OPTIONS));
    options.add(0, image.getAbsolutePath() + "[0]");
    options.add(thumbnail.getAbsolutePath());
    NativeInterface.launch(
            CONVERT_CMD, options.toArray(new String[options.size()]), thumbnail.getAbsolutePath() + ".log");
  }
}
