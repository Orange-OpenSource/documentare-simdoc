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

import com.orange.documentare.core.model.ref.comp.DistanceItem;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class DistanceGetListTest {

  @Test
  public void shouldGet() throws IOException {
    // given
    byte[] bytes1 = new byte[] { 1, 2, 3 };
    byte[] bytes2 = new byte[] { 1, 2, 3, 4, 5, 6 };
    byte[] bytes3 = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
    TestItem item1 = new TestItem(bytes1, 1);
    TestItem item2 = new TestItem(bytes2, 2);
    TestItem item3 = new TestItem(bytes3, 3);
    DistanceItem[] items = new DistanceItem[] { item2, item3 };
    Distance distance = new Distance();
    int expected1 = 666666;
    int expected2 = 750000;
    // do
    int[] distances = distance.get(item1, items);
    // then
    Assert.assertEquals(expected1, distances[0], 0);
    Assert.assertEquals(expected2, distances[1], 0);
  }
}
