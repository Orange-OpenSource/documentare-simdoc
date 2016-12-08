package com.orange.documentare.core.comp.ncd;
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
import org.junit.Assert;
import org.junit.Test;

public class NcdGetDistanceFunctionnalTest {
  private static final String smallString1 = "papa";
  private static final String smallString2 = "toto";
  private static final String stringAlphabet = "aaaabbbbccccddddeeeeffffgggghhhhiiiijjjjkkkkllllmmmmonnnpoooppppqqqqrrrrssssttttuuuuvvvvwwwwxxxxyyyyzzzz";
  private static final String stringAlphabet2 = "aaaarrrrssssttttuuuuvvvvwwwwxxxxyyyyzzzz";

  private final Ncd ncd = new Ncd();

  @Test
  public void shouldGetNullDistanceOnSameElement() {
    // given
    NcdInput elements1 = new NcdInput(stringAlphabet.getBytes());
    NcdInput elements2 = new NcdInput(stringAlphabet.getBytes());
    // do
    NcdResult ncdResult = ncd.getNcdDistance(elements1, elements2);
    // then
    Assert.assertTrue(ncdResult.getNcd() == 0f);
  }

  @Test
  public void shouldGetDistanceOnStrings() {
    // given
    NcdInput elements1 = new NcdInput(stringAlphabet.getBytes());
    NcdInput elements2 = new NcdInput(stringAlphabet.getBytes());
    NcdInput elements3 = new NcdInput(stringAlphabet.getBytes());
    NcdInput elements4 = new NcdInput(stringAlphabet2.getBytes());
    NcdInput elements5 = new NcdInput(smallString1.getBytes());
    NcdInput elements6 = new NcdInput(smallString2.getBytes());
    // do
    NcdResult ncdResult12 = ncd.getNcdDistance(elements1, elements2);
    NcdResult ncdResult34 = ncd.getNcdDistance(elements3, elements4);
    NcdResult ncdResult56 = ncd.getNcdDistance(elements5, elements6);
    // then
    Assertions.assertThat(ncdResult12.getNcd()).isEqualTo(0f);
    Assertions.assertThat(ncdResult34.getNcd()).isEqualTo(0.6969697f);
    Assertions.assertThat(ncdResult56.getNcd()).isEqualTo(2);
    Assert.assertEquals(elements1.getCompLength(), 165);
    Assert.assertEquals(elements2.getCompLength(), 165);
    Assert.assertEquals(elements3.getCompLength(), 165);
    Assert.assertEquals(elements4.getCompLength(), 60);
  }
}
