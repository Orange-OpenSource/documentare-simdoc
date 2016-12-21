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
import com.orange.documentare.core.comp.lyndonrle.LyndonRle;
import com.orange.documentare.core.comp.rle.RunLength;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Ncd {
  private final SaisBwt bwt = new SaisBwt();
  private final CompressedLength compressedLength = new RunLength();

  public NcdResult getNcdDistance(NcdInput x, NcdInput y) {
    byte[] mergedXY = getMergedXY(x.getBytes(), y.getBytes());
    int xLen = getCompLengthOf(x);
    int yLen = getCompLengthOf(y);
    int xyLen = getCompLengthOf(mergedXY);
    float ncd = getNcdDistance(xLen, yLen, xyLen);
    return new NcdResult(ncd, xLen, yLen, xyLen);
  }

  private byte[] getMergedXY(byte[] xBytes, byte[] yBytes) {
    int xLen = xBytes.length;
    int yLen = yBytes.length;
    byte[] mergedXY = new byte[xLen+yLen];
    System.arraycopy(xBytes, 0, mergedXY, 0, xLen);
    try {
      System.arraycopy(yBytes, 0, mergedXY, xLen, yLen);
    } catch (ArrayIndexOutOfBoundsException e) {
      log.error(String.format("%d %d", xLen, yLen));
    }
    return mergedXY;
  }

  private int getCompLengthOf(NcdInput input) {
    if (input.isCompLengthAvailable()) {
      return input.getCompLength();
    } else {
      return doGetCompLengthOf(input);
    }
  }

  private int doGetCompLengthOf(NcdInput input) {
    int length = getCompLengthOf(input.getBytes());
    input.setCompLength(length);
    return length;
  }

  private int getCompLengthOf(byte[] bytes) {
    byte[] bwtResult = bwt.getBwt(bytes);
    return compressedLength.getCompressedLengthOf(bwtResult);
  }

  private float getNcdDistance(int xLen, int yLen, int xyLen) {
    return (float)(xyLen - Math.min(xLen, yLen)) / Math.max(xLen, yLen);
  }
}
