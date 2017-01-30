package com.orange.documentare.core.image.segmentation;
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
import com.orange.documentare.core.image.opencv.OpencvLoader;
import com.orange.documentare.core.image.segmentation.Segmenter;
import com.orange.documentare.core.model.io.Gzip;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

@RunWith(ZohhakRunner.class)
public class SegmenterIntegrationTest {

  static {
    OpencvLoader.load();
  }

  @After
  public void cleanup() {
    FileUtils.deleteQuietly(new File("latin_segmentation.json.gz"));
    FileUtils.deleteQuietly(new File("latin_segmentation.png"));
  }

  @TestWith({
          "/latin.png, /latin_segmentation.reference.json.gz, latin_segmentation.json.gz, latin_segmentation.png"
  })
  public void doSegmentation(String image, String refFileResource, String OutputJson, String outputImage) throws IOException {
    // given
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    File ref = new File(getClass().getResource(refFileResource).getFile());
    String refJson = Gzip.getStringFromGzipFile(ref);
    Segmenter segmenter = new Segmenter(new File(getClass().getResource(image).getFile()));
    File outputFileImage = new File(outputImage);
    File OutputFileJson = new File(OutputJson);

    // when
    segmenter.doSegmentation();
    segmenter.drawSegmentation(outputFileImage);
    jsonGenericHandler.writeObjectToJsonGzipFile(segmenter.getImageSegmentation(), OutputFileJson);
    String outputJson = Gzip.getStringFromGzipFile(OutputFileJson);

    // then
    Assertions.assertThat(outputJson).isEqualTo(refJson);
  }
}
