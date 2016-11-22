package com.orange.documentare.core.model.ref.segmentation;
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

public class SegmentationRectOverlappedTest {

  @Test
  public void shouldDetectOverlapped() {
    // given
    TestRect r1 = new TestRect(10, 10, 10, 10);
    TestRect r2 = new TestRect(9, 9, 2, 2);
    TestRect r3 = new TestRect(9, 19, 2, 2);
    TestRect r4 = new TestRect(14, 14, 2, 2);
    TestRect r5 = new TestRect(9, 11, 2, 2);
    TestRect r6 = new TestRect(11, 9, 2, 2);
    // then
    Assert.assertTrue(r1.overlaps(r2));
    Assert.assertTrue(r2.overlaps(r1));
    Assert.assertTrue(r1.overlaps(r3));
    Assert.assertTrue(r3.overlaps(r1));
    Assert.assertTrue(r1.overlaps(r4));
    Assert.assertTrue(r4.overlaps(r1));
    Assert.assertTrue(r1.overlaps(r5));
    Assert.assertTrue(r5.overlaps(r1));
    Assert.assertTrue(r1.overlaps(r6));
    Assert.assertTrue(r6.overlaps(r1));
  }

  @Test
  public void shouldDetectNotOverlapped() {
    // given
    TestRect r1 = new TestRect(10, 10, 10, 10);
    TestRect r2 = new TestRect(5, 5, 2, 10);
    TestRect r3 = new TestRect(5, 5, 2, 2);
    TestRect r4 = new TestRect(5, 15, 2, 10);
    TestRect r5 = new TestRect(15, 5, 2, 2);
    // then
    Assert.assertFalse(r1.overlaps(r2));
    Assert.assertFalse(r2.overlaps(r1));
    Assert.assertFalse(r1.overlaps(r3));
    Assert.assertFalse(r3.overlaps(r1));
    Assert.assertFalse(r1.overlaps(r4));
    Assert.assertFalse(r4.overlaps(r1));
    Assert.assertFalse(r1.overlaps(r5));
    Assert.assertFalse(r5.overlaps(r1));
  }
}
