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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.orange.documentare.core.comp.bwt.SaisBwt;
import com.orange.documentare.core.comp.rle.RunLength;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class Ncd {
  private static final byte[] SIMDOC_MAGIC_NUMBER = "JoTOphe".getBytes();

  private final SaisBwt bwt = new SaisBwt();
  private final CompressedLengthMethod compressedLengthMethod = new RunLength();

  static final Cache<byte[], Integer> compressedLengthCache = CacheBuilder.newBuilder()
    .recordStats()
    .maximumSize(100000)
    .build();

  public float computeNcd(byte[] vanillaX, byte[] vanillaY) {
    byte[] taggedX = buildTaggedArray(vanillaX);
    byte[] taggedY = buildTaggedArray(vanillaY);

    int xCompressedLength = computeCompressedLengthOf(taggedX, vanillaX);
    int yCompressedLength = computeCompressedLengthOf(taggedY, vanillaY);

    // 'x == y' optimization is done outside when we do not want to check the compression method symmetry
    byte[] xy = mergeXY(taggedX, taggedY);
    // no cache optimization for xy since outside optimization should avoid computing both ncd(x, y) and ncd(y, x)
    int xyCompressedLength = doComputeCompressedLengthOf(xy);
    float ncd = computeNcd(xCompressedLength, yCompressedLength, xyCompressedLength);

    return ncd;  }

  public static CacheStats cacheStats() {
    return compressedLengthCache.stats();
  }

  private byte[] buildTaggedArray(byte[] vanilla) {
    byte[] taggedVanilla = new byte[vanilla.length + SIMDOC_MAGIC_NUMBER.length];
    System.arraycopy(SIMDOC_MAGIC_NUMBER, 0, taggedVanilla, 0, SIMDOC_MAGIC_NUMBER.length);
    System.arraycopy(vanilla, 0, taggedVanilla, SIMDOC_MAGIC_NUMBER.length, vanilla.length);
    return taggedVanilla;
  }

  private byte[] mergeXY(byte[] x, byte[] y) {
    int xLen = x.length;
    int yLen = y.length;
    byte[] xy = new byte[xLen+yLen];
    System.arraycopy(x, 0, xy, 0, xLen);
    try {
      System.arraycopy(y, 0, xy, xLen, yLen);
    } catch (ArrayIndexOutOfBoundsException e) {
      log.error(String.format("Ncd merge xy, arrays length error: %d %d", xLen, yLen));
    }
    return xy;
  }

  private int computeCompressedLengthOf(byte[] tagged, byte[] vanilla) {
    try {
      return compressedLengthCache.get(vanilla,
        () -> doComputeCompressedLengthOf(tagged));
    } catch (ExecutionException e) {
      String msg = "[ERROR] cache error: " + e.getMessage();
      log.error(msg);
      throw new IllegalStateException(msg);
    }
  }

  private int doComputeCompressedLengthOf(byte[] bytes) {
    byte[] bwtResult = bwt.getBwt(bytes);
    return compressedLengthMethod.computeCompressedLengthOf(bwtResult);
  }

  private float computeNcd(int xLen, int yLen, int xyLen) {
    return (float)(xyLen - Math.min(xLen, yLen)) / Math.max(xLen, yLen);
  }
}
