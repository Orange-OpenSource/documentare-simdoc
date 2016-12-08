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

public class OpenCvImageBytesGetRawOfTest {

  /** 2 x 3 image */
  private static final byte[] IMAGE_BYTES = {
          12, 34, 56,
          21, 43, 65
  };

  static {
    OpencvLoader.load();
  }

  @Test
  public void shouldGetRawBytesOf() {
    // given
    Mat mat = getTestMat();
    // do
    byte[] bytes = OpenCvImage.getRawBytesOf(mat);
    // then
    Assert.assertArrayEquals(IMAGE_BYTES, bytes);
  }

  private Mat getTestMat() {
    return OpenCvImage.getMatFromBinaryDat(IMAGE_BYTES, 2, 3);
  }
}
