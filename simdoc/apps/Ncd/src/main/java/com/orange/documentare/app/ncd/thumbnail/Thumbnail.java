package com.orange.documentare.app.ncd.thumbnail;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.ncd.FileToIdMapper;
import com.orange.documentare.core.comp.nativeinterface.NativeInterface;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.io.FileUtils.forceDelete;

public class Thumbnail {
  private static final File THUMBNAILS_DIR = new File("thumbnails");
  private static final String CMD = "convert";
  private static final String[] ARGS = {
    "-thumbnail", "x300", "-background white", "-alpha remove", "-polaroid -0"
  };
  private static final String[] SUPPORT_THUMBNAILS = {
          ".png", ".jpg", ".jpeg", ".tif", ".tiff", ".pdf"
  };

  private final File directory;
  private final FileToIdMapper fileToIdMapper;

  public Thumbnail(File directory, FileToIdMapper fileToIdMapper) throws IOException {
    this.directory = directory;
    this.fileToIdMapper = fileToIdMapper;
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
      forceDelete(file);
    }
  }

  private void createThumbnail(File file, ThumbnailProgress thumbnailProgress) {
    List<String> options = new ArrayList<>(Arrays.asList(ARGS));
    String thumbnailPath = THUMBNAILS_DIR.getAbsolutePath() + "/" + fileToIdMapper.idOf(file) + ".png";
    options.add(0, file.getAbsolutePath() + "[0]");
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
    String filename = file.getName().toLowerCase();
    return Arrays.asList(SUPPORT_THUMBNAILS).stream()
            .filter(extension -> filename.endsWith(extension))
            .count() > 0;
  }
}
