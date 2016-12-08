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

public class ChangeIndicesFunctionalTest {

  @Test
  public void shouldGetChangeIndices() {
    // given
    byte[] array1 = new byte[] { 1, 2, 2, 2, 3, 3, 4, 5, 5 };
    int[] expectedChangeIndices1 = { 0, 3, 3, 3, 5, 5, 6, 8, 8 };
    byte[] array2 = new byte[] {};
    int[] expectedChangeIndices2 = {};
    byte[] array3 = new byte[] { 23 };
    int[] expectedChangeIndices3 = { 0 };
    byte[] array4 = new byte[] { 23, 34, 56 };
    int[] expectedChangeIndices4 = { 0, 1, 2 };
    byte[] array5 = new byte[] { 97, 97, 97, 97, 98, 98, 98 };
    int[] expectedChangeIndices5 = { 3, 3, 3, 3, 6, 6, 6 };

    ChangeIndices ci = new ChangeIndices();
    // do
    int[] changeIndices1 = ci.getChangeIndices(array1);
    int[] changeIndices2 = ci.getChangeIndices(array2);
    int[] changeIndices3 = ci.getChangeIndices(array3);
    int[] changeIndices4 = ci.getChangeIndices(array4);
    int[] changeIndices5 = ci.getChangeIndices(array5);
    // then
    Assert.assertArrayEquals(expectedChangeIndices1, changeIndices1);
    Assert.assertArrayEquals(expectedChangeIndices2, changeIndices2);
    Assert.assertArrayEquals(expectedChangeIndices3, changeIndices3);
    Assert.assertArrayEquals(expectedChangeIndices4, changeIndices4);
    Assert.assertArrayEquals(expectedChangeIndices5, changeIndices5);
  }
}
