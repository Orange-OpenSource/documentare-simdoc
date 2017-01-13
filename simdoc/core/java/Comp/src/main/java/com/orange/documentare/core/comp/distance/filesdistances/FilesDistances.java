package com.orange.documentare.core.comp.distance.filesdistances;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.comp.distance.computer.DistancesComputer;
import com.orange.documentare.core.comp.measure.ProgressListener;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
public class FilesDistances {
  public final FileDistanceItem[] items1;
  public final FileDistanceItem[] items2;
  public final DistancesArray distancesArray;

  public static FilesDistances empty() {
    return new FilesDistances(null, null, null);
  }

  public FilesDistances compute(File directory1, File directory2, ProgressListener progressListener) throws IOException {
    boolean sameDirectory = directory1.equals(directory2);
    FileDistanceItem[] items1 = getItemsFrom(directory1);
    FileDistanceItem[] items2 = sameDirectory ? items1 : getItemsFrom(directory2);
    DistancesArray distancesArray = computeDistances(items1, items2, progressListener);

    releaseFilesBytes(items1);
    if (!sameDirectory) {
      releaseFilesBytes(items2);
    }

    return sameDirectory ?
            new FilesDistances(items1, null, distancesArray) :
            new FilesDistances(items1, items2, distancesArray);
  }

  public DistancesArray computeDistances(DistanceItem[] items1, DistanceItem[] items2, ProgressListener progressListener) {
    DistancesComputer computer = new DistancesComputer(items1, items2);
    computer.setProgressListener(progressListener);
    computer.compute();
    return computer.getDistancesArray();
  }

  private FileDistanceItem[] getItemsFrom(File directory) throws IOException {
    String rootDirectoryPath = directory.getAbsolutePath();
    Collection<File> directoryFiles = listSortedDirectoryFilesRecursively(directory);
    List<DistanceItem> items = new ArrayList<>();
    for (File directoryFile : directoryFiles) {
      if (shouldNotIgnore(directoryFile)) {
        items.add(new FileDistanceItem(directoryFile, rootDirectoryPath));
      }
    }
    FileDistanceItem[] array = new FileDistanceItem[items.size()];
    return items.toArray(array);
  }

  private boolean shouldNotIgnore(File directoryFile) throws IOException {
    return !directoryFile.isHidden();
  }

  private Collection<File> listSortedDirectoryFilesRecursively(File directory) {
    return FileUtils.listFiles(directory, null, true).stream()
            .sorted()
            .collect(Collectors.toList());
  }

  private void releaseFilesBytes(FileDistanceItem[] items) {
    Arrays.stream(items).forEach(FileDistanceItem::releaseBytes);
  }
}
