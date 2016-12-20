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

import java.util.List;

public class ChangeIndicesGetCompressedChangeIndices {

  @Test
  public void shouldGetCompressedChangeIndices() {
    // given
    byte[] array = { 1, 2, 2, 2, 3, 3, 4, 5, 5 };
    Integer[] expectedChangeIndices = { 0, 3, 5, 6, 8 };
    ChangeIndices ci = new ChangeIndices();
    // do
    List<Integer> changeIndices = ci.getCompressedChangeIndices(array);
    // then
    Assert.assertArrayEquals(expectedChangeIndices, changeIndices.toArray());
  }

  @Test
  public void shouldGetCompressedChangeIndicesForOneElement() {
    // given
    byte[] array = { 1 };
    Integer[] expectedChangeIndices = { 0 };
    ChangeIndices ci = new ChangeIndices();
    // do
    List<Integer> changeIndices = ci.getCompressedChangeIndices(array);
    // then
    Assert.assertArrayEquals(expectedChangeIndices, changeIndices.toArray());
  }
}
