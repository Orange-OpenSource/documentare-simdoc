package com.orange.documentare.core.comp.clustering.geometry;
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

public class HeronGetTest {

  @Test
  public void shouldGet1() {
    // given
    float a = 0.1f;
    float b = 0.2f;
    float c = (float) Math.sqrt(a*a + b*b);
    // do
    float area = Heron.get(a, b, c);
    // then
    Assert.assertEquals(a*b/2, area, 0.001);
  }

  @Test
  public void shouldGet2() {
    // given
    float a = 0.7f;
    float b = 0.5f;
    float c = (float) Math.sqrt(a*a + b*b);
    // do
    float area = Heron.get(a, b, c);
    // then
    Assert.assertEquals(a*b/2, area, 0.001);
  }

  @Test
  public void shouldGet3() {
    // given
    float a = 7;
    float b = 15;
    float c = 10;
    float expected = 29.39f;
    // do
    float area = Heron.get(a, b, c);
    // then
    Assert.assertEquals(expected, area, 0.01);
  }

  @Test
  public void shouldGetNullForInvalidRectangle() {
    // given
    float a = 100;
    float b = 1;
    float c = 1;
    // do
    // do
    float area = Heron.get(a, b, c);
    // then
    Assert.assertEquals(0, area, 0);
  }
}
