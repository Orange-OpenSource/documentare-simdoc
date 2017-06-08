package com.orange.documentare.app.ncdremote;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;

import java.util.stream.IntStream;

public class TestElements {

  public static BytesData[] elements1() {
    return elements(new byte[][] {new byte[]{1}, new byte[]{2}});
  }

  public static BytesData[] elements2() {
    return elements(new byte[][] {new byte[]{4}, new byte[]{1}, new byte[]{5}});
  }

  private static BytesData[] elements(byte[][] bytesArrays) {
    BytesData[] bytesData = new BytesData[bytesArrays.length];
    IntStream.range(0, bytesArrays.length).forEach(i ->
            bytesData[i] = new BytesData("" + bytesArrays[i][0], bytesArrays[i])
    );
    return bytesData;
  }

}
