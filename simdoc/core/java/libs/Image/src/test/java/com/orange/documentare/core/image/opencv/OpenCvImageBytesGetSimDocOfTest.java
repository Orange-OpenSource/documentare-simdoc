package com.orange.documentare.core.image.opencv;
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
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;

public class OpenCvImageBytesGetSimDocOfTest {

  /** 2 x 3 image */
  private static final byte[] IMAGE_BYTES = {
          12, 34, 56, OpenCvImage.SIMDOC_LINE_TERMINATION,
          21, 43, 65, OpenCvImage.SIMDOC_LINE_TERMINATION
  };

  private static final byte[] SIMDOC_IMAGE_BYTES = getSimDocImageBytes();

  static {
    OpencvLoader.load();
  }

  @Test
  public void shouldGetSimDocBytesOf() {
    // given
    Mat mat = getTestMat();
    // do
    byte[] bytes = OpenCvImage.getSimDocBytesOf(mat);
    // then
    Assert.assertArrayEquals(SIMDOC_IMAGE_BYTES, bytes);
  }

  private Mat getTestMat() {
    return OpenCvImage.getMatFromSimDocBinaryDat(SIMDOC_IMAGE_BYTES);
  }

  private static byte[] getSimDocImageBytes() {
    byte[] simDocBytes = new byte[IMAGE_BYTES.length + OpenCvImage.SIMDOC_MAGIC_NUMBER.length];
    System.arraycopy(OpenCvImage.SIMDOC_MAGIC_NUMBER, 0, simDocBytes, 0, OpenCvImage.SIMDOC_MAGIC_NUMBER.length);
    System.arraycopy(IMAGE_BYTES, 0, simDocBytes, OpenCvImage.SIMDOC_MAGIC_NUMBER.length, IMAGE_BYTES.length);
    return simDocBytes;
  }
}
