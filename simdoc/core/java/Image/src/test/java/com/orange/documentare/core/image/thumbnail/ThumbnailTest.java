package com.orange.documentare.core.image.thumbnail;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import com.orange.documentare.core.image.opencv.OpenCvImage;
import com.orange.documentare.core.image.opencv.OpencvLoader;
import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;

@RunWith(ZohhakRunner.class)
public class ThumbnailTest {

  static {
    OpencvLoader.load();
  }

  private File bigImage = new File(getClass().getResource("/bastet.png").getFile());
  private File thumbnail = new File("thumbnail.png");
  private File thumbnailSmall = new File("thumbnail-small.png");

  @After
  public void cleanup() {
    FileUtils.deleteQuietly(thumbnail);
    FileUtils.deleteQuietly(thumbnailSmall);
  }

  @TestWith({
          "file.pnG, true",
          "file.jpG, true",
          "file.jpEg, true",
          "file.tiF, true",
          "file.tiFf, true",
          "file.pDf, true",

          "png.f, false",
          "jpg.f, false",
          "jpeg.f, false",
          "tif.f, false",
          "tiff.f, false",
          "pdf.f, false"
  })
  public void supported_image_extension(String filename, boolean expectedSupported) throws IOException {
    // when
    boolean supported = Thumbnail.canCreateThumbnail(new File(filename));
    // then
    Assertions.assertThat(supported).isEqualTo(expectedSupported);
  }

  @Test(expected = NullPointerException.class)
  public void raise_exception_if_input_image_is_null() throws IOException {
    Thumbnail.createThumbnail(null, thumbnail);
  }

  @Test(expected = IOException.class)
  public void raise_exception_if_input_image_is_not_readable() throws IOException {
    Thumbnail.createThumbnail(new File("/pouet-pouet"), thumbnail);
  }

  @Test(expected = NullPointerException.class)
  public void raise_exception_if_output_image_is_null() throws IOException {
    Thumbnail.createThumbnail(bigImage, null);
  }

  @Test
  public void create_thumbnail_of_a_big_image() throws IOException {
    // Given

    // When
    Thumbnail.createThumbnail(bigImage, thumbnail);

    // Then
    Mat mat = OpenCvImage.loadMat(thumbnail);
    Assertions.assertThat(mat.size().width).isEqualTo(151);
    Assertions.assertThat(mat.size().height).isEqualTo(200);
  }

  @Test
  public void thumbnail_of_a_small_image_has_same_size() throws IOException {
    // Given
    Thumbnail.createThumbnail(bigImage, thumbnail);
    // When
    Thumbnail.createThumbnail(thumbnail, thumbnailSmall);

    // Then
    Mat mat = OpenCvImage.loadMat(thumbnailSmall);
    Assertions.assertThat(mat.size().width).isEqualTo(151);
    Assertions.assertThat(mat.size().height).isEqualTo(200);
  }
}
