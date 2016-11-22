package com.orange.documentare.core.image.segmentationcollection;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.segmentation.SegmentationRect.Size;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SegmentationCollectionGetCentralMeanSizeTest {

  @Test
  public void shouldGetCentralMeanSize3() {
    // given
    TestRect r1 = new TestRect(0, 0, 10, 10);
    TestRect r2 = new TestRect(0, 0, 5, 6);
    TestRect r3 = new TestRect(0, 0, 9, 11);
    List<TestRect> rects = new ArrayList<>();
    rects.add(r1);
    rects.add(r2);
    rects.add(r3);

    // do
    Size mean = SegmentationCollection.getCentralMeanSize(rects);

    // then
    Assert.assertEquals(8, mean.width(), 0);
    Assert.assertEquals(9, mean.height(), 0);
  }

  @Test
  public void shouldGetCentralMeanSize6() {
    // given
    TestRect r1 = new TestRect(0, 0, 10, 10);
    TestRect r2 = new TestRect(0, 0, 5, 9);
    TestRect r3 = new TestRect(0, 0, 9, 9);
    TestRect r4 = new TestRect(0, 0, 1, 1);
    TestRect r5 = new TestRect(0, 0, 11, 11);
    TestRect r6 = new TestRect(0, 0, 2, 2);
    List<TestRect> rects = new ArrayList<>();
    rects.add(r1);
    rects.add(r2);
    rects.add(r3);
    rects.add(r4);
    rects.add(r5);
    rects.add(r6);

    // do
    Size mean = SegmentationCollection.getCentralMeanSize(rects);

    // then
    Assert.assertEquals(3, mean.width(), 0);
    Assert.assertEquals(5, mean.height(), 0);
  }
}
