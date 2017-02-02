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

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.opencv.core.Mat;

public class OpenCvImageBytesToMatTest {

  /** 2 x 3 image */
  private static final byte[] IMAGE_BYTES = {
          12, 34, 56,
          21, 43, 65
  };

  static {
    OpencvLoader.load();
  }

  @Test
  public void build_opencv_mat_from_raw_bytes() {
    // given
    Mat mat = OpenCvImage.bytesToMat(IMAGE_BYTES, 2, 3);
    // do
    byte[] bytes = OpenCvImage.matToBytes(mat);
    // then
    Assertions.assertThat(bytes).isEqualTo(IMAGE_BYTES);
  }
}
