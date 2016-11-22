package com.orange.documentare.app.ncd.thumbnail;
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
import com.orange.documentare.app.ncd.FileToIdMapper;
import org.fest.assertions.Assertions;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

@RunWith(ZohhakRunner.class)
public class ThumbnailTest {

  private final Thumbnail thumbnail = new Thumbnail(new File("test"), new FileToIdMapper());

  public ThumbnailTest() throws IOException {
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
  public void supports_thumbnail(String filename, boolean expectedSupported) {
    // given

    // when
    boolean supported = thumbnail.canCreateThumbnail(new File(filename));

    // then
    Assertions.assertThat(supported).isEqualTo(expectedSupported);
  }
}
