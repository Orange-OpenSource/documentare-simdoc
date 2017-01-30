package com.orange.documentare.core.image.connectedcomponents;
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
import com.orange.documentare.core.image.linedetection.Lines;
import com.orange.documentare.core.image.test.TestDrawer;
import com.orange.documentare.core.image.opencv.OpencvLoader;
import com.orange.documentare.core.model.io.Gzip;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

@RunWith(ZohhakRunner.class)
public class ConnectedComponentsDetectorIntegrationTest {

  static {
    OpencvLoader.load();
  }

  @After
  public void cleanup() {
    FileUtils.deleteQuietly(new File("latin_connected_components.json.gz"));
    FileUtils.deleteQuietly(new File("latin_connected_components.png"));
  }

  @TestWith({
          "/latin.png, /latin_connected_components_ref.json.gz, latin_connected_components.json.gz, /latin.png, latin_connected_components.png"
  })
  public void detectConnectedComponentsOnImage(String image, String refFileResource, String OutputFile, String inputImage, String outputImage) throws IOException {
    // given
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    File ccRefFile = new File(getClass().getResource(refFileResource).getFile());
    String ccRefJsonString = Gzip.getStringFromGzipFile(ccRefFile);
    File imageFile = new File(getClass().getResource(image).getFile());
    ConnectedComponentsDetector connectedComponentsDetector = new ConnectedComponentsDetector();
    File outputFile = new File(OutputFile);

    // when
    ConnectedComponents connectedComponents = connectedComponentsDetector.detect(imageFile);
    TestDrawer.draw(new File(getClass().getResource(inputImage).getFile()), new File(outputImage), new Lines(), connectedComponents);

    jsonGenericHandler.writeObjectToJsonGzipFile(connectedComponents, outputFile);
    String ccTestJsonString = Gzip.getStringFromGzipFile(outputFile);

    // then
    Assertions.assertThat(ccTestJsonString).isEqualTo(ccRefJsonString);
  }
}
