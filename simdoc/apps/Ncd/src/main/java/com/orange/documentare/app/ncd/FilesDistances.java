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

import com.orange.documentare.app.ncd.memory.MemoryWatcher;
import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.comp.distance.computer.DistancesComputer;
import com.orange.documentare.core.comp.measure.Progress;
import com.orange.documentare.core.comp.measure.ProgressListener;
import com.orange.documentare.core.comp.measure.TreatmentStep;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
class FilesDistances implements ProgressListener {
  private static final String IGNORE_FILE = ".ds_store";

  /** directory to directory comparison */
  public RegularFilesDistances handleDirectoriesDistanceMatrix(File directory1, File directory2) throws IOException {
    boolean sameDirectory = directory1.equals(directory2);
    NcdItem[] items1 = getItemsFrom(directory1);
    NcdItem[] items2 = sameDirectory ? items1 : getItemsFrom(directory2);
    DistancesArray distancesArray = computeDistances(items1, items2);

    releaseFilesBytes(items1);
    if (!sameDirectory) {
      releaseFilesBytes(items2);
    }
    forceMemoryReleaseForMonitoringPurpose();

    return sameDirectory ?
            new RegularFilesDistances(items1, distancesArray) :
            new RegularFilesDistances(items1, items2, distancesArray);
  }

  DistancesArray computeDistances(DistanceItem[] items1, DistanceItem[] items2) {
    DistancesComputer computer = new DistancesComputer(items1, items2);
    computer.setProgressListener(this);
    computer.compute();
    MemoryWatcher.watch();
    return computer.getDistancesArray();
  }

  private NcdItem[] getItemsFrom(File directory) throws IOException {
    String rootDirectoryPath = directory.getAbsolutePath();
    Collection<File> directoryFiles = listSortedDirectoryFilesRecursively(directory);
    List<DistanceItem> items = new ArrayList<>();
    for (File directoryFile : directoryFiles) {
      if (shouldNotIgnore(directoryFile)) {
        items.add(new NcdItem(directoryFile, rootDirectoryPath));
      }
    }
    NcdItem[] array = new NcdItem[items.size()];
    return items.toArray(array);
  }

  private boolean shouldNotIgnore(File directoryFile) {
    return !directoryFile.getName().toLowerCase().contains(IGNORE_FILE);
  }

  @Override
  public void onProgressUpdate(TreatmentStep step, Progress progress) {
    System.out.print("\r" + progress.displayString(step.toString()));
  }

  private Collection<File> listSortedDirectoryFilesRecursively(File directory) {
    return FileUtils.listFiles(directory, null, true).stream()
            .sorted()
            .collect(Collectors.toList());
  }

  private void releaseFilesBytes(NcdItem[] items) {
    Arrays.asList(items).forEach(item -> item.setBytes(null));
  }

  private void forceMemoryReleaseForMonitoringPurpose() {
    System.gc();
  }
}
