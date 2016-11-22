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

import org.fest.assertions.Assertions;
import org.junit.Test;

public class SegmentationRectIsADiacriticOfTest {

  @Test
  public void smallAndBelowIsADiacriticOfLargeAndAbove() {
    // given
    TestRect largeAndAbove = new TestRect(0, 10, 10, 10);
    TestRect smallAndBelow = new TestRect(4, 4, 2, 2);

    // when / then
    Assertions.assertThat(smallAndBelow.isADiacriticOf(largeAndAbove)).isTrue();
  }

  @Test
  public void largeAndAboveIsNotADiacriticOfSmallAndBelow() {
    // given
    TestRect largeAndAbove = new TestRect(0, 10, 10, 10);
    TestRect smallAndBelow = new TestRect(4, 4, 2, 2);

    // when / then
    Assertions.assertThat(largeAndAbove.isADiacriticOf(smallAndBelow)).isFalse();
  }

  @Test
  public void overlapOnRightBottomIsADiacriticOfRect() {
    // given
    TestRect rect = new TestRect(0, 10, 10, 10);
    TestRect overlapOnRightBottom = new TestRect(8, 8, 10, 4);

    // when / then
    Assertions.assertThat(overlapOnRightBottom.isADiacriticOf(rect)).isTrue();
  }

  @Test
  public void rectIsNotADiacriticOfOverlapOnRightBottom() {
    // given
    TestRect rect = new TestRect(0, 10, 10, 10);
    TestRect overlapOnRightBottom = new TestRect(8, 8, 10, 4);

    // when / then
    Assertions.assertThat(rect.isADiacriticOf(overlapOnRightBottom)).isFalse();
  }

  @Test
  public void rectOnTheRightisNotADiacriticOfRect() {
    // given
    TestRect rect = new TestRect(0, 10, 10, 10);
    TestRect rectOnTheRight = new TestRect(12, 10, 10, 10);

    // when / then
    Assertions.assertThat(rectOnTheRight.isADiacriticOf(rect)).isFalse();
  }

  @Test
  public void rectOnTheLeftIsNotADiacriticOfRect() {
    // given
    TestRect rectOnTheLeft = new TestRect(0, 10, 10, 10);
    TestRect rect = new TestRect(12, 10, 10, 10);

    // when / then
    Assertions.assertThat(rectOnTheLeft.isADiacriticOf(rect)).isFalse();
  }
}
