package com.orange.documentare.app.prepclustering;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.prepclustering.cmdline.CommandLineException;
import com.orange.documentare.app.prepclustering.cmdline.CommandLineOptions;
import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.comp.distance.matrix.DistancesMatrixCsvGzipWriter;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.OptionalInt;

public class PrepClusteringApp {

  private static final String MATRIX = "ncd_matrix.csv.gz";
  private static final String NEAREST = "ncd_nearests.csv.gz";
  private static final File CLUSTERING_EXPORT_FILE = new File("prep_clustering_ready.json.gz");

  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
    try {
      doTheJob(new CommandLineOptions(args));
    } catch (CommandLineException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void doTheJob(CommandLineOptions commandLineOptions) throws IOException {
    RegularFilesDistances regularFilesDistances = loadRegularFilesDistances(commandLineOptions.getFile());
    boolean useTriangulationVertices = commandLineOptions.isUseTriangulationVertices();
    if (!useTriangulationVertices) {
      writeCSVFiles(regularFilesDistances);
    }
    if (regularFilesDistances.getDistancesArray().isOnSameArray()) {
      writeJsonForClustering(regularFilesDistances, useTriangulationVertices);
    }
    System.out.println("\n[Done]");
  }

  private static void writeCSVFiles(RegularFilesDistances regularFilesDistances) throws IOException {
    DistanceItem[] items1 = regularFilesDistances.getItems1();
    DistanceItem[] items2  = regularFilesDistances.getItems2();
    items2 = items2 == null ? items1 : items2;
    DistancesArray distancesArray = regularFilesDistances.getDistancesArray();

    DistancesMatrixCsvGzipWriter csvWriter = new DistancesMatrixCsvGzipWriter(items1, items2, distancesArray);
    System.out.println("[Write matrix CSV]");
    csvWriter.writeTo(new File(MATRIX), false);
    System.out.println("[Write nearest CSV]");
    csvWriter.writeTo(new File(NEAREST), true);
  }

  private static void writeJsonForClustering(RegularFilesDistances regularFilesDistances, boolean useTriangulationVertices) throws IOException {
    System.out.println("[Export clustering model]");
    ClusteringModel clusteringModel = new ClusteringModel(regularFilesDistances, useTriangulationVertices);
    exportToJson(clusteringModel, CLUSTERING_EXPORT_FILE);
  }

  private static RegularFilesDistances loadRegularFilesDistances(File file) throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
    RegularFilesDistances regularFilesDistances = (RegularFilesDistances)
            jsonGenericHandler.getObjectFromJsonGzipFile(RegularFilesDistances.class, file);
    regularFilesDistances.updateHumanReadableId();
    return regularFilesDistances;
  }

  private static void exportToJson(Object object, File file) throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    jsonGenericHandler.writeObjectToJsonGzipFile(object, file);
  }
}
