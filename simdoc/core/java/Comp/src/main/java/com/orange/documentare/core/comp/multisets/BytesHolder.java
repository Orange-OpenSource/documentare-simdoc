package com.orange.documentare.core.comp.multisets;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import java.util.ArrayList;
import java.util.List;

/** maintains a list of byte arrays, and the total number of bytes backed */
class BytesHolder {
  private List<byte[]> bytesArrays = new ArrayList<>();
  private int bytesCount = 0;

  void add(byte[] bytes) {
    bytesArrays.add(bytes);
    bytesCount += bytes.length;
  }

  byte[] flatten() {
    byte[] flatten = new byte[bytesCount];
    bytesCount = 0;
    for (byte[] bytes : bytesArrays) {
      System.arraycopy(bytes, 0, flatten, bytesCount, bytes.length);
      bytesCount += bytes.length;
    }
    return flatten;
  }
}
