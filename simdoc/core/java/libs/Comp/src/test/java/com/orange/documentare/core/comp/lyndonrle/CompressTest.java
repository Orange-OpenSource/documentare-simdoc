package com.orange.documentare.core.comp.lyndonrle;
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

public class CompressTest {

  private final LyndonRle lyndonRle = new LyndonRle();

  private final String[] inputs = {
          "aaaa",
          "aaba",
          "baaa",
          "aaaabaabbbbbab",
          "abracadabra",
          "baaaaabababababa",
          "9ème étage à droite, layette, sextoys et autres accessoires, ouverture de 2230 à 2310 à partir du 31022016"
  };

  private final byte[][] expected = {
          new byte[] {0, 0, 0, 4, 'a'},
          new byte[] {0, 0, 0, 2, 'a', 0, 0, 0, 1, 'b', 0, 0, 0, 1, 'a'},
          new byte[] {0, 0, 0, 1, 'b', 0, 0, 0, 3, 'a'},
          new byte[] {0, 0, 0, 4, 'a', 0, 0, 0, 1, 'b',  0, 0, 0, 2, 'a', 0, 0, 0, 5, 'b', 0, 0, 0, 1, 'a', 'b'},
          new byte[] {0, 0, 0, 1, 'a', 'b', 'r', 0, 0, 0, 1, 'a', 'c', 0, 0, 0, 1, 'a', 'd', 0, 0, 0, 1, 'a', 'b', 'r', 0, 0, 0, 1, 'a'},
          new byte[] {0, 0, 0, 1, 'b', 0, 0, 0, 5, 'a', 0, 0, 0, 1, 'b', 0, 0, 0, 4, 'a', 'b', 0, 0, 0, 1, 'a'},
          new byte[] {0, 0, 0, 1, 57, 0, 0, 0, 1, -61, 0, 0, 0, 1, -88, 109, 0, 0, 0, 1, 101, 0, 0, 0, 1, 32, 0, 0, 0, 1, -61, 0, 0, 0, 1, -87, 116, 0, 0, 0, 1, 97, 103, 0, 0, 0, 1, 101, 0, 0, 0, 1, 32, 0, 0, 0, 1, -61, 0, 0, 0, 1, -96, 32, 100, 114, 0, 0, 0, 1, 111, 0, 0, 0, 1, 105, 116, 0, 0, 0, 1, 101, 0, 0, 0, 1, 44, 0, 0, 0, 1, 32, 108, 0, 0, 0, 1, 97, 121, 0, 0, 0, 1, 101, 116, 116, 0, 0, 0, 1, 101, 0, 0, 0, 1, 44, 0, 0, 0, 1, 32, 115, 0, 0, 0, 1, 101, 120, 0, 0, 0, 1, 116, 0, 0, 0, 1, 111, 121, 0, 0, 0, 1, 115, 0, 0, 0, 1, 32, 101, 116, 0, 0, 0, 1, 32, 97, 117, 0, 0, 0, 1, 116, 0, 0, 0, 1, 114, 0, 0, 0, 1, 101, 115, 0, 0, 0, 1, 32, 97, 99, 99, 101, 115, 115, 0, 0, 0, 1, 111, 0, 0, 0, 1, 105, 114, 0, 0, 0, 1, 101, 115, 0, 0, 0, 1, 44, 0, 0, 0, 1, 32, 111, 117, 118, 0, 0, 0, 1, 101, 114, 116, 117, 0, 0, 0, 1, 114, 0, 0, 0, 1, 101, 0, 0, 0, 1, 32, 100, 101, 0, 0, 0, 1, 32, 50, 50, 51, 0, 0, 0, 1, 48, 0, 0, 0, 1, 32, 0, 0, 0, 1, -61, 0, 0, 0, 1, -96, 32, 50, 51, 0, 0, 0, 1, 49, 0, 0, 0, 1, 48, 0, 0, 0, 1, 32, 0, 0, 0, 1, -61, 0, 0, 0, 1, -96, 32, 112, 0, 0, 0, 1, 97, 114, 116, 0, 0, 0, 1, 105, 114, 0, 0, 0, 1, 32, 100, 117, 0, 0, 0, 1, 32, 51, 0, 0, 0, 1, 49, 0, 0, 0, 1, 48, 50, 50, 0, 0, 0, 1, 48, 49, 54},
  };

  // FIXME REMOVE or FIX @Test
  public void compress() {
    // given / do / then
    for (int i = 0; i < inputs.length; i++) {
      test(inputs[i], expected[i]);
    }
  }

  public void test(String input, byte[] expected) {
    // given
    // do
    byte[] compressed = lyndonRle.compress(input.getBytes());
    // then
    Assertions.assertThat(compressed).isEqualTo(expected);
  }
}
