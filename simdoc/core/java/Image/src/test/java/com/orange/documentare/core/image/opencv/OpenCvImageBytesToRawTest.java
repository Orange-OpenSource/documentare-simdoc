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

public class OpenCvImageBytesToRawTest {

  /** 2 x 3 image */
  private static final byte[] RAW_IMAGE_BYTES = {
          12, 34, 56, '\n',
          21, 43, 65, '\n'
  };

  static {
    OpencvLoader.load();
  }

  @Test
  public void convert_from_raw_image_bytes_to_open_cv_mat() {
    // given
    Mat mat = OpenCvImage.rawToMat(RAW_IMAGE_BYTES);
    // do
    byte[] bytes = OpenCvImage.matToRaw(mat);
    // then
    Assertions.assertThat(bytes).isEqualTo(RAW_IMAGE_BYTES);
  }
}
