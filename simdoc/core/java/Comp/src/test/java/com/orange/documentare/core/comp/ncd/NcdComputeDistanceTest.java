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

public class NcdComputeDistanceTest {
  private static final String smallString1 = "papa";
  private static final String smallString2 = "toto";
  private static final String stringAlphabet = "aaaabbbbccccddddeeeeffffgggghhhhiiiijjjjkkkkllllmmmmonnnpoooppppqqqqrrrrssssttttuuuuvvvvwwwwxxxxyyyyzzzz";
  private static final String stringAlphabet2 = "aaaarrrrssssttttuuuuvvvvwwwwxxxxyyyyzzzz";

  private final Ncd ncd = new Ncd();

  @Test
  public void compression_not_available_before_computation() {
    // given
    // do
    NcdInput ncdInput = new NcdInput(stringAlphabet.getBytes());
    // then
    Assert.assertFalse(ncdInput.compressedLengthAvailable);
  }

  @Test
  public void compression_available_after_computation() {
    // given
    NcdInput ncdInput = new NcdInput(stringAlphabet.getBytes());
    // do
    NcdResult ncdResult = ncd.computeNcd(ncdInput, ncdInput);
    ncdInput = ncdInput.withCompression(ncdResult.input1CompressedLength);
    // then
    Assert.assertTrue(ncdInput.compressedLengthAvailable);
  }

  @Test
  public void distance_on_same_ncd_input() {
    // given
    NcdInput element = new NcdInput(stringAlphabet.getBytes());
    // do
    NcdResult ncdResult = ncd.computeNcd(element, element);
    // then
    Assert.assertTrue(ncdResult.ncd == 0f);
  }


  @Test
  public void distance_on_distinct_inputs_but_same_data_is_null() {
    // given
    NcdInput input1 = new NcdInput(stringAlphabet.getBytes());
    NcdInput input2 = new NcdInput(stringAlphabet.getBytes());
    // do
    NcdResult ncdResult = ncd.computeNcd(input1, input2);
    // then
    Assert.assertTrue(ncdResult.ncd == 0f);
  }

  @Test
  public void compute_distance_on_strings() {
    // given
    NcdInput[] inputs = buildInputs();

    // do
    NcdResult ncdResult01 = ncd.computeNcd(inputs[0], inputs[1]);
    NcdResult ncdResult23 = ncd.computeNcd(inputs[2], inputs[3]);
    NcdResult ncdResult45 = ncd.computeNcd(inputs[4], inputs[5]);

    inputs[0] = inputs[0].withCompression(ncdResult01.input1CompressedLength);
    inputs[1] = inputs[1].withCompression(ncdResult01.input2CompressedLength);
    inputs[2] = inputs[2].withCompression(ncdResult23.input1CompressedLength);
    inputs[3] = inputs[3].withCompression(ncdResult23.input2CompressedLength);
    inputs[4] = inputs[4].withCompression(ncdResult45.input1CompressedLength);
    inputs[5] = inputs[5].withCompression(ncdResult45.input2CompressedLength);

    // then
    Assertions.assertThat(ncdResult01.ncd).isEqualTo(0f);
    Assertions.assertThat(ncdResult23.ncd).isEqualTo(0.6969697f);
    Assertions.assertThat(ncdResult45.ncd).isEqualTo(2);
    Assert.assertEquals(inputs[0].compressedLength, 165);
    Assert.assertEquals(inputs[1].compressedLength, 165);
    Assert.assertEquals(inputs[2].compressedLength, 165);
    Assert.assertEquals(inputs[3].compressedLength, 60);
  }

  private NcdInput[] buildInputs() {
    NcdInput[] elements =  {
      new NcdInput(stringAlphabet.getBytes()),
      new NcdInput(stringAlphabet.getBytes()),
      new NcdInput(stringAlphabet.getBytes()),
      new NcdInput(stringAlphabet2.getBytes()),
      new NcdInput(smallString1.getBytes()),
      new NcdInput(smallString2.getBytes())
    };
    return elements;
  }
}
