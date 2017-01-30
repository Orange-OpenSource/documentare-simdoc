package com.orange.documentare.core.comp.ncd;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.bwt.SaisBwt;
import com.orange.documentare.core.comp.rle.RunLength;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Ncd {
  private final SaisBwt bwt = new SaisBwt();
  private final CompressedLengthMethod compressedLengthMethod = new RunLength();

  public NcdResult computeNcd(NcdInput input1, NcdInput input2) {
    int input1CompressedLength = computeCompressedLengthOf(input1);
    int input2CompressedLength = computeCompressedLengthOf(input2);

    // 'input1 == input2' optimization is done outside when we do not want to check the compression method symmetry
    byte[] mergedInputs = mergeInputs(input1.bytes, input2.bytes);
    int mergedInputsCompressedLength = computeCompressedLengthOf(mergedInputs);
    float ncd = computeNcd(input1CompressedLength, input2CompressedLength, mergedInputsCompressedLength);

    return new NcdResult(ncd, input1CompressedLength, input2CompressedLength);
  }

  private byte[] mergeInputs(byte[] input1, byte[] input2) {
    int input1Len = input1.length;
    int input2Len = input2.length;
    byte[] mergedInputs = new byte[input1Len+input2Len];
    System.arraycopy(input1, 0, mergedInputs, 0, input1Len);
    try {
      System.arraycopy(input2, 0, mergedInputs, input1Len, input2Len);
    } catch (ArrayIndexOutOfBoundsException e) {
      log.error(String.format("Ncd merge inputs, arrays length error: %d %d", input1Len, input2Len));
    }
    return mergedInputs;
  }

  private int computeCompressedLengthOf(NcdInput input) {
    return input.compressedLengthAvailable ?
      input.compressedLength :
      computeCompressedLengthOf(input.bytes);
  }

  private int computeCompressedLengthOf(byte[] bytes) {
    byte[] bwtResult = bwt.getBwt(bytes);
    return compressedLengthMethod.computeCompressedLengthOf(bwtResult);
  }

  private float computeNcd(int xLen, int yLen, int xyLen) {
    return (float)(xyLen - Math.min(xLen, yLen)) / Math.max(xLen, yLen);
  }
}
