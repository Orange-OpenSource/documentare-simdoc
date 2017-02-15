package com.orange.documentare.core.image.thumbnail;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Thumbnail {
  private static final File THUMBNAILS_DIR = new File("thumbnails");
  private static final String CMD = "convert";
  private static final String[] ARGS = {
    "-thumbnail", "x300", "-background white", "-alpha remove", "-polaroid -0"
  };
  private static final String[] SUPPORT_THUMBNAILS = {
          ".png", ".jpg", ".jpeg", ".tif", ".tiff", ".bmp", ".pdf"
  };

  private final File directory;

  public Thumbnail(File directory) throws IOException {
    this.directory = directory;
    FileUtils.deleteDirectory(THUMBNAILS_DIR);
    THUMBNAILS_DIR.mkdir();
  }

  public void createDirectoryFilesThumbnails() throws IOException {
    System.out.println();

    String[] extensions = null;
    boolean recursively = true;
    Collection<File> files = FileUtils.listFiles(directory, extensions, recursively);

    List<File> readyForThumbnails = files.stream()
            .filter(file -> canCreateThumbnail(file))
            .collect(Collectors.toList());

    ThumbnailProgress thumbnailProgress = new ThumbnailProgress(readyForThumbnails.size());
    readyForThumbnails.parallelStream()
            .forEach(file -> createThumbnail(file, thumbnailProgress));

    clearLog();
  }

  private void clearLog() throws IOException {
    String[] extensions = { "log" };
    boolean recursively = false;
    Collection<File> files = FileUtils.listFiles(THUMBNAILS_DIR, extensions, recursively);
    for (File file : files) {
      FileUtils.forceDelete(file);
    }
  }

  private void createThumbnail(File file, ThumbnailProgress thumbnailProgress) {
    List<String> options = new ArrayList<>(Arrays.asList(ARGS));
    String thumbnailPath = THUMBNAILS_DIR.getAbsolutePath() + "/" + file.getName() + ".png";
    options.add(0, file.getAbsolutePath() + "\\[0\\]");
    options.add(thumbnailPath);
    NativeInterface.launch(
            CMD, options.toArray(new String[options.size()]), thumbnailPath + ".log");

    showProgress(thumbnailProgress);
  }

  private void showProgress(ThumbnailProgress thumbnailProgress) {
    thumbnailProgress.newThumbnailCreated();
    System.out.print("\r" + thumbnailProgress.progress().displayString("Thumbnails"));
  }

  boolean canCreateThumbnail(File file) {
    File target;
    try {
      target = Files.isSymbolicLink(file.toPath()) ?
        Files.readSymbolicLink(file.toPath()).toFile() :
        file;
    } catch (IOException e) {
      log.error("[Thumbnails] Failed to read file", e);
      return false;
    }

    String filename = target.getName().toLowerCase();
    return Arrays.asList(SUPPORT_THUMBNAILS).stream()
            .filter(extension -> filename.endsWith(extension))
            .count() > 0;
  }
}
