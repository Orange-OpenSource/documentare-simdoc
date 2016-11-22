package com.orange.documentare.core.comp.rle;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.ncd.CompressedLength;

import java.util.ArrayList;
import java.util.List;

public class RunLength implements CompressedLength {

  public int getCompressedLengthOf(byte[] inputArray) {
    int encodedLength = 0;
    for (int i = 0; i < inputArray.length; i++) {
      while (isElementEqualToNextOne(inputArray, i)) {
        i++;
      }
      encodedLength += 5;
    }
    return encodedLength;
  }

  public List<Byte> encode(byte[] inputArray) {
    List<Byte> output = new ArrayList<>();
    for (int i = 0; i < inputArray.length; i++) {
      int runLength = 1;
      while (isElementEqualToNextOne(inputArray, i)) {
        runLength++;
        i++;
      }
      addIntegerToByteArray(output, runLength);
      output.add(inputArray[i]);
    }
    return output;
  }

  private void addIntegerToByteArray(List<Byte> output, int runLength) {
    output.add((byte)(runLength & 0xff));
    output.add((byte)(runLength>>8 & 0xff));
    output.add((byte)(runLength>>16 & 0xff));
    output.add((byte)(runLength>>24 & 0xff));
  }

  private boolean isElementEqualToNextOne(byte[] inputArray, int i) {
    boolean hasNext = i+1 < inputArray.length;
    if (hasNext) {
      return inputArray[i] == inputArray[i + 1];
    } else {
      return false;
    }
  }
}
