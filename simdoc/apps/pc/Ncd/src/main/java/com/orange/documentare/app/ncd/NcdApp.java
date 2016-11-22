package com.orange.documentare.app.ncd;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.ncd.cmdline.CommandLineException;
import com.orange.documentare.app.ncd.cmdline.CommandLineOptions;
import com.orange.documentare.app.ncd.memory.MemoryWatcher;
import com.orange.documentare.app.ncd.thumbnail.Thumbnail;
import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;

public class NcdApp {

  private static final File SIMDOC_EXPORT_FILE = new File("ncd_simdoc_model_ready_for_clustering.json.gz");
  private static final File REGULAR_FILES_EXPORT_FILE = new File("ncd_regular_files_model.json.gz");

  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
    try {
      doTheJob(new CommandLineOptions(args));
    } catch (CommandLineException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void doTheJob(CommandLineOptions commandLineOptions) throws IOException {
    if (commandLineOptions.getSimDocJsonGz() != null) {
      doTheJobForSimDocInput(commandLineOptions.getSimDocJsonGz());
    } else {
      doTheJobForRegularFiles(commandLineOptions.getFile1(), commandLineOptions.getFile2());
    }
    System.out.println("\n[Done]");
  }

  private static void doTheJobForRegularFiles(File file1, File file2) throws IOException {
    FileToIdMapper fileToIdMapper = new FileToIdMapper();
    FilesDistances filesDistances = new FilesDistances(fileToIdMapper);

    RegularFilesDistances regularFilesDistances = filesDistances.handleDirectoriesDistanceMatrix(file1, file2);

    System.out.println("\n[Export regular files distances]");
    exportToJson(regularFilesDistances, REGULAR_FILES_EXPORT_FILE);

    MemoryWatcher.stopWatching();

    boolean sameDirectories = file1.equals(file2);
    if (sameDirectories) {
      Thumbnail thumbnail = new Thumbnail(file1, fileToIdMapper);
      thumbnail.createDirectoryFilesThumbnails();

      fileToIdMapper.writeMappingCsv();
    }
  }

  private static void doTheJobForSimDocInput(File simDocJsonGz) throws IOException {
    ImageSegmentation imageSegmentation = segmentationOf(simDocJsonGz);
    DigitalTypes digitalTypes = imageSegmentation.getDigitalTypes();

    DigitalTypes copyWithoutSpaces = digitalTypes.copyWithoutSpaces();
    DigitalType[] items = copyWithoutSpaces.toArray(new DigitalType[copyWithoutSpaces.size()]);

    FilesDistances filesDistances = new FilesDistances();
    DistancesArray distancesArray = filesDistances.computeDistances(items, items);

    for (int i = 0; i < copyWithoutSpaces.size(); i++) {
      DigitalType digitalType = copyWithoutSpaces.get(i);
      digitalType.setNearestItems(distancesArray.nearestItemsFor(copyWithoutSpaces, i));
      digitalType.setBytes(null);
    }

    System.out.println("\n[Export Simdoc model]");
    exportToJson(imageSegmentation, SIMDOC_EXPORT_FILE);

    MemoryWatcher.stopWatching();
  }

  private static ImageSegmentation segmentationOf(File simDocJsonGz) throws IOException {
    JsonGenericHandler jsonHandler = new JsonGenericHandler(true);
    return (ImageSegmentation) jsonHandler.getObjectFromJsonGzipFile(ImageSegmentation.class, simDocJsonGz);
  }

  private static void exportToJson(Object object, File file) throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    jsonGenericHandler.writeObjectToJsonGzipFile(object, file);
  }
}
