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

import java.io.File;
import java.io.IOException;

public class OpenCvImageRawTransformationTest {

  static {
    OpencvLoader.load();
  }

  @Test
  public void build_raw() throws IOException {
    // given
    File image = new File(getClass().getResource("/big-image.jpg").getFile());
    Mat mat = OpenCvImage.loadMat(image);
    // do
    byte[] bytes = OpenCvImage.matToRaw(mat);
    // then
    Assertions.assertThat(bytes).hasSize(2286900);
  }

  @Test
  public void build_raw_with_rescaling() throws IOException {
    // given
    File image = new File(getClass().getResource("/big-image.jpg").getFile());
    Mat mat = OpenCvImage.loadMat(image);
    int bytesSizeTarget = 100 * 1024;
    // do
    Mat rescaledMat = OpenCvImage.resize(mat, bytesSizeTarget);
    byte[] rescaledBytes = OpenCvImage.matToRaw(rescaledMat);
    // then
    Assertions.assertThat(rescaledBytes).hasSize(bytesSizeTarget);
  }
}
