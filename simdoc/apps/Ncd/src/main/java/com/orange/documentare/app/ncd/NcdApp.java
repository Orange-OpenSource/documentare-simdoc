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

import com.orange.documentare.app.ncd.cmdline.CommandLineOptions;
import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.comp.distance.filesdistances.FilesDistances;
import com.orange.documentare.core.comp.measure.ProgressListener;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import com.orange.documentare.core.system.measure.MemoryWatcher;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;

public class NcdApp {

  private static final File SIMDOC_EXPORT_FILE = new File("ncd_simdoc_model_ready_for_clustering.json.gz");
  private static final File FILES_DISTANCES_EXPORT_FILE = new File("ncd_regular_files_model.json.gz");

  private static final ProgressListener progressListener =
    (step, progress) -> System.out.print("\r" + progress.displayString(step.toString()));


  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
    System.out.println("\n[NCD - Start]");
    CommandLineOptions options;
    try {
      options = new CommandLineOptions(args);
    } catch(Exception e) {
      CommandLineOptions.showHelp();
      return;
    }
    try {
      doTheJob(options);
      System.out.println("[NCD - Done]");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void doTheJob(CommandLineOptions commandLineOptions) throws IOException {
    MemoryWatcher.watch();
    if (commandLineOptions.getSimdoc() != null) {
      doTheJobForSimDocInput(commandLineOptions.getSimdoc());
    } else {
      doTheJobForRegularFiles(commandLineOptions.getD1(), commandLineOptions.getD2());
    }
    MemoryWatcher.stopWatching();
  }

  private static void doTheJobForRegularFiles(File file1, File file2) throws IOException {
    FilesDistances filesDistances = FilesDistances.empty();
    filesDistances = filesDistances.compute(file1, file2, progressListener);

    System.out.println("\n[Export files distances]");
    exportToJson(filesDistances, FILES_DISTANCES_EXPORT_FILE);
  }

  private static void doTheJobForSimDocInput(File simDocJsonGz) throws IOException {
    ImageSegmentation imageSegmentation = segmentationOf(simDocJsonGz);
    DigitalTypes digitalTypes = imageSegmentation.getDigitalTypes();

    DigitalTypes copyWithoutSpaces = digitalTypes.copyWithoutSpaces();
    DigitalType[] items = copyWithoutSpaces.toArray(new DigitalType[copyWithoutSpaces.size()]);

    FilesDistances filesDistances = FilesDistances.empty();
    DistancesArray distancesArray = filesDistances.computeDistances(items, items, progressListener);

    for (int i = 0; i < copyWithoutSpaces.size(); i++) {
      DigitalType digitalType = copyWithoutSpaces.get(i);
      digitalType.setNearestItems(distancesArray.nearestItemsFor(copyWithoutSpaces, i));
      digitalType.setBytes(null);
    }

    System.out.println("\n[Export Simdoc model]");
    exportToJson(imageSegmentation, SIMDOC_EXPORT_FILE);
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
