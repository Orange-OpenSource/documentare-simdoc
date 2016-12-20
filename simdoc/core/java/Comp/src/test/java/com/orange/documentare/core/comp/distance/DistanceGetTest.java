package com.orange.documentare.core.comp.distance;
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

import java.io.IOException;

public class DistanceGetTest {

  @Test
  public void shouldGet() throws IOException {
    // given
    byte[] bytes1 = new byte[] { 1, 2, 3 };
    byte[] bytes2 = new byte[] { 1, 2, 3, 4, 5, 6 };
    TestItem item1 = new TestItem(bytes1, 1);
    TestItem item2 = new TestItem(bytes2, 2);
    Distance distance = new Distance();
    int expected = 666666;
    // do
    int ncdResult = distance.get(item1, item2);
    // then
    Assert.assertEquals(expected, ncdResult);
  }

  @Test
  public void shouldGet0() throws IOException {
    // given
    byte[] bytes1 = new byte[] { 1, 2, 3 };
    byte[] bytes2 = new byte[] { 1, 2, 3 };
    TestItem item1 = new TestItem(bytes1, 1);
    TestItem item2 = new TestItem(bytes2, 2);
    Distance distance = new Distance();
    int expected = 0;
    // do
    int ncdResult = distance.get(item1, item2);
    // then
    Assert.assertEquals(expected, ncdResult);
  }
}
