package com.orange.documentare.core.system.inputfilesconverter.thumbnail;
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
import com.orange.documentare.core.system.nativeinterface.NativeException;
import com.orange.documentare.core.system.thumbnail.Thumbnail;
import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

@RunWith(ZohhakRunner.class)
public class ThumbnailTest {

  private File image = new File(getClass().getResource("/bastet.png").getFile());
  private File thumbnail = new File("thumbnail.png");

  @After
  public void cleanup() {
    FileUtils.deleteQuietly(thumbnail);
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

  @Test(expected = NativeException.class)
  public void raise_exception_if_input_image_is_not_readable() throws IOException {
    Thumbnail.createThumbnail(new File("/pouet-pouet"), thumbnail);
  }

  @Test(expected = NullPointerException.class)
  public void raise_exception_if_output_image_is_null() throws IOException {
    Thumbnail.createThumbnail(image, null);
  }

  @Test
  public void create_thumbnail() throws IOException {
    // When
    Thumbnail.createThumbnail(image, thumbnail);
    // Then
    Assertions.assertThat(thumbnail.exists()).isTrue();
  }
}
