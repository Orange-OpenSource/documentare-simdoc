package com.orange.documentare.core.image.segmentation;
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

public class SpaceDetectionGetCentralMeanTest {

  @Test
  public void shouldGetCentralMean() {
    // given
    int[] gaps1 = new int[] { 10 };
    int[] gaps2 = new int[] { 10, 20 };
    int[] gaps3 = new int[] { 10, 20, 30 };
    int[] gaps4 = new int[] { 0, 0, 20, 30, 40, 30, 150, 200 };
    SpaceDetection spaceDetection = new SpaceDetection();
    // do
    int meanGap1 = spaceDetection.getCentralMean(gaps1);
    int meanGap2 = spaceDetection.getCentralMean(gaps2);
    int meanGap3 = spaceDetection.getCentralMean(gaps3);
    int meanGap4 = spaceDetection.getCentralMean(gaps4);
    // then
    Assert.assertEquals(10, meanGap1);
    Assert.assertEquals(10, meanGap2);
    Assert.assertEquals(15, meanGap3);
    Assert.assertEquals(30, meanGap4);
  }
}
