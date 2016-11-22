package com.orange.documentare.core.comp.distance.matrix;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.google.common.primitives.Ints;
import com.orange.documentare.core.comp.distance.Distance;
import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPOutputStream;

@Log4j2
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class DistancesMatrixCsvGzipWriter {
  private static final String MATRIX_SEP = ";  ";

  private final DistanceItem[] items;
  private final DistanceItem[] itemsToCompare;
  private final DistancesArray distancesArray;

  public void writeTo(File file, boolean nearestNeighboursMode) throws IOException {
    OutputStream outputStream = new GZIPOutputStream(new FileOutputStream(file));
    List<String> sortedRowNames = getSortedInputItemsNames(items);
    List<String> sortedColNames = getSortedInputItemsNames(itemsToCompare);
    if (!nearestNeighboursMode) {
      showMatrixHeader(outputStream, sortedColNames);
    }
    showMatrixRows(outputStream, sortedRowNames, nearestNeighboursMode);
    outputStream.close();
  }

  private List<String> getSortedInputItemsNames(DistanceItem[] itemsToSort) {
    List<String> names = new ArrayList<>();
    for(DistanceItem item : itemsToSort) {
      names.add(item.getHumanReadableId());
    }
    Collections.sort(names);
    return names;
  }

  private void showMatrixHeader(OutputStream outputStream, List<String> names) throws IOException {
    writeStringTo(outputStream, MATRIX_SEP);
    for (String name : names) {
      writeStringTo(outputStream, String.format("%s%s", name, MATRIX_SEP));
    }
    writeStringTo(outputStream, "\n");
  }

  private void showMatrixRows(OutputStream outputStream, List<String> sortedNames, boolean nearestNeighboursMode) {
    for(String name : sortedNames) {
      int itemIndex = getItemIndexForName(name);
      showRow(outputStream, itemIndex, nearestNeighboursMode);
    }
  }

  private int getItemIndexForName(String inputName) {
    for(int i = 0; i < items.length; i++) {
      if (items[i].getHumanReadableId().equals(inputName)) {
        return i;
      }
    }
    throw new IllegalStateException(String.format("Failed to find input item with name %s", inputName));
  }

  private void showRow(OutputStream outputStream, int itemIndex, boolean nearestNeighboursMode) {
    int[] distances = distancesArray.getDistancesFor(itemIndex);
    int[] distancesIndices = getDistancesIndices(distances.length, itemIndex, nearestNeighboursMode);
    writeStringTo(outputStream, String.format("%s%s", items[itemIndex].getHumanReadableId(), MATRIX_SEP));
    showCells(outputStream, itemIndex, distancesIndices, nearestNeighboursMode);
    writeStringTo(outputStream, "\n");
  }

  private void showCells(OutputStream outputStream, int itemIndex, int[] distancesIndices, boolean nearestNeighboursMode) {
      if (nearestNeighboursMode) {
        showNearestsCells(outputStream, itemIndex, distancesIndices);
      } else {
        showMatrixCells(outputStream, itemIndex, distancesIndices);
      }
  }

  private void showMatrixCells(OutputStream outputStream, int itemIndex, int[] distancesIndices) {
    for(int i = 0; i < itemsToCompare.length; i++) {
      int distance = distancesArray.get(itemIndex, distancesIndices[i]);
        showMatrixCell(outputStream, distance);
    }
  }

  private void showNearestsCells(OutputStream outputStream, int itemIndex, int[] distancesIndices) {
    for(int i = 0; i < itemsToCompare.length; i++) {
      int distance = distancesArray.get(itemIndex, distancesIndices[i]);
        showNearestsCell(outputStream, itemsToCompare[distancesIndices[i]].getHumanReadableId(), distance);
    }
  }

  private int[] getDistancesIndices(int length, int itemIndex, boolean nearestNeighboursMode) {
    if (nearestNeighboursMode) {
      NearestItem[] nearestItems = distancesArray.nearestItemsFor(Arrays.asList(itemsToCompare), itemIndex);
      return getNearestIndicesFrom(nearestItems);
    } else {
      int[] indices = initDistancesIndices(length);
      sortDistanceByName(indices);
      return indices;
    }
  }

  private int[] getNearestIndicesFrom(NearestItem[] nearestItems) {
    int[] nearestIndices = new int[nearestItems.length];
    for (int i = 0; i < nearestIndices.length; i++) {
      nearestIndices[i] = nearestItems[i].getIndex();
    }
    return nearestIndices;
  }

  private void sortDistanceByName(int[] distancesIndices) {
    List<Integer> indices = Ints.asList(distancesIndices);
    Collections.sort(indices, new DistanceByNameIndexComparator(itemsToCompare));
  }

  private int[] initDistancesIndices(int length) {
    int[] indices = new int[length];
    for (int i = 0; i < length; i++) {
      indices[i] = i;
    }
    return indices;
  }

  private void showMatrixCell(OutputStream outputStream, int distance) {
    writeStringTo(outputStream, String.format("%f%s", getOutputDistanceFormat(distance), MATRIX_SEP));
  }

  private void showNearestsCell(OutputStream outputStream, String nearestName, int distance) {
    writeStringTo(outputStream, String.format("%s %f%s", nearestName, getOutputDistanceFormat(distance), MATRIX_SEP));
  }

  private double getOutputDistanceFormat(int distance) {
    return ((double)distance)/Distance.DISTANCE_INT_CONV_FACTOR;
  }

  private void writeStringTo(OutputStream outputStream, String string) {
    try {
      outputStream.write(string.getBytes());
    } catch (IOException e) {
      log.fatal("Failed to write to output file, " + e.getMessage(), e);
    }
  }
}
