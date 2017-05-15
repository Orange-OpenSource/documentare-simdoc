package com.orange.documentare.app.ncdremote;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.ncdremote.cmdline.CommandLineOptions;
import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import com.orange.documentare.core.comp.distance.bytesdistances.BytesDistances;
import com.orange.documentare.core.comp.measure.ProgressListener;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.prepdata.PreppedBytesData;
import com.orange.documentare.core.system.measure.MemoryWatcher;
import com.orange.documentare.app.ncdremote.MatrixDistancesSegments.MatrixDistancesSegment;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class NcdRemoteApp {

  @RequiredArgsConstructor
  static class ResultToExport {
    final Object o;
    final File file;
  }

  private static final File FILES_DISTANCES_EXPORT_FILE = new File("ncd_regular_files_model.json.gz");

  private static final ProgressListener progressListener =
    (step, progress) -> System.out.print("\r" + progress.displayString(step.toString()));

  private static JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);

  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
    System.out.println("\n[NCD REMOTE - Start]");
    CommandLineOptions options;
    try {
      options = new CommandLineOptions(args);
    } catch(Exception e) {
      CommandLineOptions.showHelp(e);
      return;
    }
    try {
      doTheJob(options);
      System.out.println("[NCD REMOTE - Done]");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void doTheJob(CommandLineOptions o) throws IOException {
    MemoryWatcher.watch();

    ResultToExport resultToExport = doTheJobForRegularFiles(o.getD1(), o.getD2(), o.getJ1(), o.getJ2());
    System.out.println("\n[Export model]");
    // Optim: bytes allocated in do* functions are available for the garbage collector now!
    exportToJson(resultToExport);

    MemoryWatcher.stopWatching();
  }

  private static ResultToExport doTheJobForRegularFiles(File directory1, File directory2, File json1, File json2) throws IOException {
    BytesData[] bytesData1;
    BytesData[] bytesData2;
    if (json1 == null) {
      bytesData1 = BytesData.loadFromDirectory(directory1, BytesData.relativePathIdProvider(directory1));
      bytesData2 = directory1.equals(directory2) ?
        bytesData1 : BytesData.loadFromDirectory(directory2, BytesData.relativePathIdProvider(directory2));
    } else {
      bytesData1 = loadPreppedBytesData(json1);
      bytesData2 = json1.equals(json2) ? bytesData1 : loadPreppedBytesData(json2);
    }

    MatrixDistancesSegments matrixDistancesSegments = new MatrixDistancesSegments(bytesData1, bytesData2);
    matrixDistancesSegments = matrixDistancesSegments.buildSegments();

    RemoteDistancesSegments remoteDistancesSegments = new RemoteDistancesSegments(matrixDistancesSegments);
    List<MatrixDistancesSegment> segments = remoteDistancesSegments.compute();

    ExportModel exportModel = new ExportModel(bytesData1, bytesData2, segments);
    return new ResultToExport(exportModel, FILES_DISTANCES_EXPORT_FILE);
  }

  private static BytesData[] loadPreppedBytesData(File json) throws IOException {
    return ((PreppedBytesData)jsonGenericHandler.getObjectFromJsonFile(PreppedBytesData.class, json)).bytesData;
  }

  private static void exportToJson(ResultToExport resultToExport) throws IOException {
    jsonGenericHandler.writeObjectToJsonGzipFile(resultToExport.o, resultToExport.file);
  }
}
