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

import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an 'array', this class computes what we call a 'Change indices' array.
 * This 'Change indices' array has the same length as the byte array.
 * For a given index 'i', the 'Change indices' array value at 'i' contains the next
 * consecutive index 'j' in the array which has the same value, but for which
 * 'j+1' value is different.
 *
 * Example:
 *  array         = { 1, 2, 2, 2, 3, 3, 4, 5, 5 }
 *  changeIndices = { 0, 3, 3, 3, 5, 5, 6, 8, 8 }
 */
class ChangeIndices {

  int[] getChangeIndices(byte[] array) {
    if (array.length == 0) {
      return new int[0];
    } else {
      return doGetChangeIndices(array);
    }
  }

  private int[] doGetChangeIndices(byte[] array) {
    List<Integer> compressedChangeIndices = getCompressedChangeIndices(array);
    return getFlattenChangeIndicesFrom(compressedChangeIndices, array.length);
  }

  @VisibleForTesting
  List<Integer> getCompressedChangeIndices(byte[] array) {
    List<Integer> compressedChangeIndices = new ArrayList<>();
    int i = 0;
    while (i < array.length-1) {
      if (array[i] != array[i+1]) {
        compressedChangeIndices.add(i);
      }
      i++;
    }
    compressedChangeIndices.add(array.length-1);
    return compressedChangeIndices;
  }

  @VisibleForTesting
  int[] getFlattenChangeIndicesFrom(List<Integer> compressedChangeIndices, int length) {
    int[] changeIndices = new int[length];
    int changeIndex = compressedChangeIndices.get(0);
    for (int i = 0; i <= changeIndex; i++) {
      changeIndices[i] = changeIndex;
    }
    for (int k = 1; k < compressedChangeIndices.size(); k++) {
      changeIndex = compressedChangeIndices.get(k);
      for (int i = compressedChangeIndices.get(k-1) + 1; i <= compressedChangeIndices.get(k); i++) {
        changeIndices[i] = changeIndex;
      }
    }
    return changeIndices;
  }
}
