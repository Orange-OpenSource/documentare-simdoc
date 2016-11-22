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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(suppressConstructorProperties = true)
class ByteArrayCompare {
  private final int[] changeIndices;

  boolean isGreaterThanInitialIndices(int initialIndex1, int initialIndex2, byte[] readOnlyArray) {
    return compare(initialIndex1, initialIndex2, readOnlyArray) > 0;
  }
  boolean equals(int initialIndex1, int initialIndex2, byte[] readOnlyArray) {
    return compare(initialIndex1, initialIndex2, readOnlyArray) == 0;
  }

  private int compare(int initialIndex1, int initialIndex2, byte[] readOnlyArray) {
    final int arrayLength = readOnlyArray.length;
    int loopIndex = 0;
    int index1 = initialIndex1;
    int index2 = initialIndex2;
    byte b1;
    byte b2;
    int i1;
    int i2;
    int inc1;
    int inc2;
    int inc;
    while (loopIndex < arrayLength) {
      b1 = readOnlyArray[index1];
      b2 = readOnlyArray[index2];
      if (b1 != b2) {
        i1 = (int) b1 & 0xFF;
        i2 = (int) b2 & 0xFF;
        return i1 - i2;
      }
      inc1 = changeIndices[index1] - index1 + 1;
      inc2 = changeIndices[index2] - index2 + 1;
      inc = Math.min(inc1, inc2);
      index1 += inc;
      index2 += inc;
      index1 = index1 >= arrayLength ? index1 - arrayLength : index1;
      index2 = index2 >= arrayLength ? index2 - arrayLength : index2;
      loopIndex += inc;
    }
    return 0;
  }
}
