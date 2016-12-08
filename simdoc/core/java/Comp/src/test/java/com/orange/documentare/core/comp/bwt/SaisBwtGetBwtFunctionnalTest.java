package com.orange.documentare.core.comp.bwt;
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

public class SaisBwtGetBwtFunctionnalTest {

  private final SaisBwt bwt = new SaisBwt();

  @Test
  public void shouldGetBwtForByteArray() {
    doTestForByteArray(BwtGetBWTransformOfFunctionnalTest.TEST_STRING_0.getBytes(), BwtGetBWTransformOfFunctionnalTest.EXPECTED_STRING_0.getBytes());
    doTestForByteArray(BwtGetBWTransformOfFunctionnalTest.TEST_STRING_1.getBytes(), BwtGetBWTransformOfFunctionnalTest.EXPECTED_STRING_1.getBytes());
    doTestForByteArray(BwtGetBWTransformOfFunctionnalTest.TEST_STRING_2.getBytes(), BwtGetBWTransformOfFunctionnalTest.EXPECTED_STRING_2.getBytes());
    doTestForByteArray(BwtGetBWTransformOfFunctionnalTest.TEST_STRING_3.getBytes(), BwtGetBWTransformOfFunctionnalTest.EXPECTED_STRING_3.getBytes());
    doTestForByteArray(BwtGetBWTransformOfFunctionnalTest.TEST_STRING_4.getBytes(), BwtGetBWTransformOfFunctionnalTest.EXPECTED_STRING_4.getBytes());
    doTestForByteArray(BwtGetBWTransformOfFunctionnalTest.TEST_STRING_5.getBytes(), BwtGetBWTransformOfFunctionnalTest.EXPECTED_STRING_5.getBytes());
    doTestForByteArray(BwtGetBWTransformOfFunctionnalTest.TEST_STRING_6.getBytes(), BwtGetBWTransformOfFunctionnalTest.EXPECTED_STRING_6.getBytes());

    doTestForByteArray(BwtGetBWTransformOfFunctionnalTest.BYTES_LONG, BwtGetBWTransformOfFunctionnalTest.BYTES_LONG_EXPECTED);
  }

  private void doTestForByteArray(byte[] array, byte[] bytesExpected) {
    // do
    byte[] bwtArray = bwt.getBwt(array);
    // then
    Assert.assertArrayEquals(bytesExpected, bwtArray);
  }
}
