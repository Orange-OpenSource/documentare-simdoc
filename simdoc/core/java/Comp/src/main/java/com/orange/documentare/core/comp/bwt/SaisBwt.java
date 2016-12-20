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

import lombok.extern.slf4j.Slf4j;

/** Bwt implementation using the SAIS (suffix array) algorithm */
@Slf4j
public class SaisBwt {
  private static final boolean ALLOW_NATIVE = false;

  private static boolean lazyLoadingDone;
  private static boolean nativeLibraryLoaded;

  public SaisBwt() {
    if (!lazyLoadingDone && ALLOW_NATIVE) {
      SaisNativeLibraryLoader loader = new SaisNativeLibraryLoader();
      nativeLibraryLoaded = loader.loadNativeLibrarySafely();
      lazyLoadingDone = true;
    }
  }

  native byte[] getBwtNative(byte[] bytes);

  public byte[] getBwt(byte[] bytes) {
    if (nativeLibraryLoaded) {
      byte[] nativeBytes = getBwtNative(bytes);
      if (nativeBytes == null) {
        log.error("OOM on native side");
        return new byte[0];
      } else {
        return nativeBytes;
      }
    } else {
      return getBwtJava(bytes);
    }
  }

  private byte[] getBwtJava(byte[] bytes) {
    byte[] saisInput = getDoubleBytesCopy(bytes);
    int length = saisInput.length;
    int[] suffixArrayIndices = new int[length];
    VanillaSais.suffixsort(saisInput, suffixArrayIndices, length);

    byte[] outputBytes = new byte[length/2];
    int outputIndex = 0;
    for (int saIndex : suffixArrayIndices) {
      if (saIndex < length / 2) {
        int origIndex = saIndex - 1;
        origIndex = origIndex < 0 ? length / 2 - 1 : origIndex;
        outputBytes[outputIndex++] = bytes[origIndex];
      }
    }
    return outputBytes;
  }

  private static byte[] getDoubleBytesCopy(byte[] bytes) {
    int length = bytes.length;
    byte[] doubleBytes = new byte[length*2];
    System.arraycopy(bytes, 0, doubleBytes, 0, length);
    System.arraycopy(bytes, 0, doubleBytes, length, length);
    return doubleBytes;
  }
}
