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

import com.orange.documentare.core.comp.distance.Distance;
import org.junit.Assert;
import org.junit.Test;

public class EquilateralityGetTest {

  @Test
  public void shouldGet1Big() {
    // given
    int a = Distance.DISTANCE_INT_CONV_FACTOR;
    int b = a;
    int c = a;
    // do
    float q = Equilaterality.get(Heron.get(a, b, c), a, b, c);
    // then
    Assert.assertEquals(q, 1, 0);
  }

  @Test
  public void shouldGet1Small() {
    // given
    int a = 1;
    int b = a;
    int c = a;
    // do
    float q = Equilaterality.get(Heron.get(a, b, c), a, b, c);
    // then
    Assert.assertEquals(q, 1, 0);
  }

  @Test
  public void shouldNotGet1() {
    // given
    int a = 10;
    int b = 10;
    int c = 5;
    // do
    float q = Equilaterality.get(Heron.get(a, b, c), a, b, c);
    // then
    Assert.assertEquals(q, 0.80, 0.01);
  }

  @Test
  public void shouldGetRealDat1() {
    // given
    int a = 450000;
    int b = 430656;
    int c = 535714;
    // do
    float q = Equilaterality.get(Heron.get(a, b, c), a, b, c);
    // then
    Assert.assertEquals(q, 0.969, 0.001);
  }
}
