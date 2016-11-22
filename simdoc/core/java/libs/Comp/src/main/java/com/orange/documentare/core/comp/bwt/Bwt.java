package com.orange.documentare.core.comp.bwt;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

/** Burrows–Wheeler transform */
public class Bwt {

  private final BwtDualPivotQuicksort bwtSort = new BwtDualPivotQuicksort();

  /**
   * Do the Burrows–Wheeler transform
   * @param readOnlyArray
   * @return BW transformed array
   */
  public byte[] getBWTransformOf(byte[] readOnlyArray) {
    int[] sortedIndices = bwtSort.sort(readOnlyArray);
    return getLastColumnOfRotMatrix(sortedIndices, readOnlyArray);
  }

  private byte[] getLastColumnOfRotMatrix(int[] sortedIndices, byte[] readOnlyArray) {
    byte[] lastColumn = new byte[readOnlyArray.length];
    fillLastColumn(lastColumn, sortedIndices, readOnlyArray);
    return lastColumn;
  }

  private void fillLastColumn(byte[] lastColumn, int[] sortedIndices, byte[] readOnlyArray) {
    for (int sortedIndex = 0; sortedIndex < readOnlyArray.length; sortedIndex++) {
      int initialLastColumnIndex = getLastColumnInitialIndexFor(sortedIndex, sortedIndices);
      lastColumn[sortedIndex] = readOnlyArray[initialLastColumnIndex];
    }
  }

  private int getLastColumnInitialIndexFor(int sortedIndex, int[] sortedIndices) {
    int initialIndex = sortedIndices[sortedIndex];
    return initialIndex == 0 ? sortedIndices.length - 1 : initialIndex - 1;
  }
}
