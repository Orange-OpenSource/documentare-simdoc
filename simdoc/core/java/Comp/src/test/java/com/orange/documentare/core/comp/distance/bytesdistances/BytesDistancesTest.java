package com.orange.documentare.core.comp.distance.bytesdistances;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Denis Boisset & Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.distance.DistancesArray;
import org.fest.assertions.Assertions;
import org.junit.Test;

public class BytesDistancesTest {

  @Test
  public void compute_distances_between_two_arrays() {
    // Given
    byte[] bytes1 = { 1, 2};
    byte[] bytes2 = { 3, 4};
    BytesData bytesData1 = new BytesData("1", bytes1);
    BytesData bytesData2 = new BytesData("2", bytes2);
    BytesData[] bytesDataArray1 = {bytesData1};
    BytesData[] bytesDataArray2 = {bytesData2};
    BytesDistances bytesDistances = new BytesDistances();

    DistancesArray expectedDistancesArray = new DistancesArray(1, 1, false);
    expectedDistancesArray.setDistancesForItem(0, new int[]{333333});

    // When
    DistancesArray distancesArray = bytesDistances.computeDistancesBetweenCollections(bytesDataArray1, bytesDataArray2);

    // Then
    Assertions.assertThat(distancesArray).isEqualTo(expectedDistancesArray);
  }

  @Test
  public void compute_distances_between_array_elements() {
    // Given
    byte[] bytes1 = { 1, 2};
    byte[] bytes2 = { 3, 4};
    BytesData bytesData1 = new BytesData("1", bytes1);
    BytesData bytesData2 = new BytesData("2", bytes2);
    BytesData[] bytesDataArray = {bytesData1, bytesData2};
    BytesDistances bytesDistances = new BytesDistances();

    // NB: due to optimization on same array, matrix is represented as a triangle internally...
    DistancesArray expectedDistancesArray = new DistancesArray(2, 2, true);
    expectedDistancesArray.setDistancesForItem(0, new int[]{333333});
    expectedDistancesArray.setDistancesForItem(1, new int[]{});

    // When
    DistancesArray distancesArray = bytesDistances.computeDistancesInCollection(bytesDataArray);

    // Then
    Assertions.assertThat(distancesArray).isEqualTo(expectedDistancesArray);
  }
}
