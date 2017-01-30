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
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class DistanceComputeTest {

  @Test
  public void compute_distance_of_two_items() throws IOException {
    // given
    byte[] bytes1 = new byte[] { 1, 2, 3 };
    byte[] bytes2 = new byte[] { 1, 2, 3, 4, 5, 6 };
    TestItem item1 = new TestItem(bytes1, 1);
    TestItem item2 = new TestItem(bytes2, 2);
    Distance distance = new Distance();
    int expected = 666666;
    // do
    int ncdResult = distance.compute(item1, item2);
    // then
    Assertions.assertThat(ncdResult).isEqualTo(expected);

    // make sure inputs map is updated correctly for optimization purpose
    Assertions.assertThat(distance.inputsMap.get(item1).compressedLengthAvailable).isTrue();
    Assertions.assertThat(distance.inputsMap.get(item2).compressedLengthAvailable).isTrue();
  }

  @Test
  public void distance_for_same_data_should_be_null() throws IOException {
    // given
    byte[] bytes1 = new byte[] { 1, 2, 3 };
    byte[] bytes2 = new byte[] { 1, 2, 3 };
    TestItem item1 = new TestItem(bytes1, 1);
    TestItem item2 = new TestItem(bytes2, 2);
    Distance distance = new Distance();
    int expected = 0;
    // do
    int ncdResult = distance.compute(item1, item2);
    // then
    Assert.assertEquals(expected, ncdResult);
  }

  @Test
  public void compute_distance_to_all_items() throws IOException {
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
    int[] distances = distance.compute(item1, items);
    // then
    Assertions.assertThat(distances[0]).isEqualTo(expected1);
    Assertions.assertThat(distances[1]).isEqualTo(expected2);

    // make sure internal map has been updated
    Assertions.assertThat(distance.inputsMap.get(item1).compressedLengthAvailable).isTrue();
    Assertions.assertThat(distance.inputsMap.get(item2).compressedLengthAvailable).isTrue();
    Assertions.assertThat(distance.inputsMap.get(item3).compressedLengthAvailable).isTrue();
  }
}
