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

import com.orange.documentare.core.model.ref.segmentation.SegmentationRect;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SegmentationCollectionGetSizeOrderedRangeTest {

  @Test
  public void shouldGetSizeOrderedRange() {
    // given
    TestRect r1 = new TestRect(0, 0, 10, 10);
    TestRect r2 = new TestRect(0, 0, 5, 6);
    TestRect r3 = new TestRect(0, 0, 9, 8);
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
    List<SegmentationRect> orderedRangeAll = SegmentationCollection.getSizeOrderedRange(rects, 0f, 1f);
    List<SegmentationRect> ordered25 = SegmentationCollection.getSizeOrderedRange(rects, 0.25f, 0.75f);

    // then
    Assert.assertEquals(r4, orderedRangeAll.get(0));
    Assert.assertEquals(r6, orderedRangeAll.get(1));
    Assert.assertEquals(r2, orderedRangeAll.get(2));
    Assert.assertEquals(r3, orderedRangeAll.get(3));
    Assert.assertEquals(r1, orderedRangeAll.get(4));

    Assert.assertEquals(r6, ordered25.get(0));
    Assert.assertEquals(r2, ordered25.get(1));
  }
}
