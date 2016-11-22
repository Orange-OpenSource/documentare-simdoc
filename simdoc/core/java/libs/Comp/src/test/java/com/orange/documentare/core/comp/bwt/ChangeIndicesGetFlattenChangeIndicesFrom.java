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

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ChangeIndicesGetFlattenChangeIndicesFrom {

  @Test
  public void shouldGetFlattenChangeIndicesFrom() {
    // given
    Integer[] compressedChangeIndices = new Integer[] { 0, 3, 5, 6, 8 };
    int[] expectedChangeIndices = { 0, 3, 3, 3, 5, 5, 6, 8, 8 };
    ChangeIndices ci = new ChangeIndices();
    // do
    int[] changeIndices = ci.getFlattenChangeIndicesFrom(Arrays.asList(compressedChangeIndices), expectedChangeIndices.length);
    // then
    Assert.assertArrayEquals(expectedChangeIndices, changeIndices);
  }

  @Test
  public void shouldGetFlattenChangeIndicesFromWithOneElement() {
    // given
    Integer[] compressedChangeIndices = new Integer[] { 0 };
    int[] expectedChangeIndices = { 0 };
    ChangeIndices ci = new ChangeIndices();
    // do
    int[] changeIndices = ci.getFlattenChangeIndicesFrom(Arrays.asList(compressedChangeIndices), expectedChangeIndices.length);
    // then
    Assert.assertArrayEquals(expectedChangeIndices, changeIndices);
  }
}
