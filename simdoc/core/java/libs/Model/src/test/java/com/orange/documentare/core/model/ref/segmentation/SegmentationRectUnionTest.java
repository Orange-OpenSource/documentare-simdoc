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

public class SegmentationRectUnionTest {

  @Test
  public void changeNothingIfProvidedRectIsInside() {
    // given
    TestRect r1 = new TestRect(0, 0, 10, 10);
    TestRect r2 = new TestRect(4, 4, 2, 2);
    TestRect expected12 = new TestRect(r1);
    // when
    r1.union(r2);
    // then
    Assertions.assertThat(r1).isEqualTo(expected12);
  }

  @Test
  public void matchProvidedRectIfInsideIt() {
    // given
    TestRect r1 = new TestRect(1, 1, 1, 1);
    TestRect r2 = new TestRect(0, 0, 3, 3);
    TestRect expected12 = new TestRect(r2);
    // when
    r1.union(r2);
    // then
    Assertions.assertThat(r1).isEqualTo(expected12);
  }

  @Test
  public void grow() {
    // given
    TestRect r1 = new TestRect(0, 0, 4, 4);
    TestRect r2 = new TestRect(2, 2, 8, 8);
    TestRect expected12 = new TestRect(0, 0, 10, 10);
    // when
    r1.union(r2);
    // then
    Assertions.assertThat(r1).isEqualTo(expected12);
  }

  @Test(expected = IllegalArgumentException.class)
  public void throwExceptionIfArgumentIsNull() {
    // Given
    TestRect rect = new TestRect(0, 0, 3, 3);

    // When
    rect.union(null);

    // Then
    // raised exception
  }
}
