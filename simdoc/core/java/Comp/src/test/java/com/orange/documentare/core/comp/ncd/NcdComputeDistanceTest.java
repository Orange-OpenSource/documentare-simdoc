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

import java.util.Arrays;

public class NcdComputeDistanceTest {
  private static final String smallString1 = "papa";
  private static final String smallString2 = "toto";
  private static final String stringAlphabet = "aaaabbbbccccddddeeeeffffgggghhhhiiiijjjjkkkkllllmmmmonnnpoooppppqqqqrrrrssssttttuuuuvvvvwwwwxxxxyyyyzzzz";
  private static final String stringAlphabet2 = "aaaarrrrssssttttuuuuvvvvwwwwxxxxyyyyzzzz";

  @Test
  public void compression_length_available_after_computation() {
    // given
    Ncd ncd = new Ncd();
    byte[] bytes = stringAlphabet.getBytes();
    // do
    ncd.computeNcd(bytes, bytes);
    // then
    Assertions.assertThat(ncd.compressedLengthCache.get(bytes)).isEqualTo(165);
  }

  @Test
  public void distance_on_same_ncd_input() {
    // given
    Ncd ncd = new Ncd();
    byte[] bytes = stringAlphabet.getBytes();
    // do
    float ncdResult = ncd.computeNcd(bytes, bytes);
    // then
    Assert.assertTrue(ncdResult == 0f);
  }


  @Test
  public void distance_on_distinct_inputs_but_same_data_is_null() {
    // given
    Ncd ncd = new Ncd();
    byte[] bytes1 = stringAlphabet.getBytes();
    byte[] bytes2 = Arrays.copyOf(stringAlphabet.getBytes(), stringAlphabet.length());
    // do
    float ncdResult = ncd.computeNcd(bytes1, bytes2);
    // then
    Assert.assertTrue(ncdResult == 0f);
  }

  @Test
  public void compute_distance_on_strings() {
    // given
    Ncd ncd = new Ncd();
    byte[][] inputs = buildInputs();

    // do
    float ncdResult01 = ncd.computeNcd(inputs[0], inputs[1]);
    float ncdResult23 = ncd.computeNcd(inputs[2], inputs[3]);
    float ncdResult45 = ncd.computeNcd(inputs[4], inputs[5]);

    // then
    Assertions.assertThat(ncdResult01).isEqualTo(0f);
    Assertions.assertThat(ncdResult23).isEqualTo(0.6969697f);
    Assertions.assertThat(ncdResult45).isEqualTo(2);
    Assertions.assertThat(ncd.compressedLengthCache.get(inputs[0])).isEqualTo(165);
    Assertions.assertThat(ncd.compressedLengthCache.get(inputs[1])).isEqualTo(165);
    Assertions.assertThat(ncd.compressedLengthCache.get(inputs[2])).isEqualTo(165);
    Assertions.assertThat(ncd.compressedLengthCache.get(inputs[3])).isEqualTo(60);
  }

  private byte[][] buildInputs() {
    byte[][] elements =  {
      stringAlphabet.getBytes(),
      stringAlphabet.getBytes(),
      stringAlphabet.getBytes(),
      stringAlphabet2.getBytes(),
      smallString1.getBytes(),
      smallString2.getBytes()
    };
    return elements;
  }
}
