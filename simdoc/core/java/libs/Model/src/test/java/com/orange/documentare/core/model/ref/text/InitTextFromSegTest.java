package com.orange.documentare.core.model.ref.text;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class InitTextFromSegTest {

  private static final String OUTPUT = "init-image-text.json";

  @Test
  public void should_init_imageText_from_segmentation() throws IOException {
    // given
    String refFileResource = "/seg_stripped.json";
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    File ref = new File(getClass().getResource(refFileResource).getFile());
    ImageSegmentation imageSegmentation = (ImageSegmentation) jsonGenericHandler.getObjectFromJsonFile(ImageSegmentation.class, ref);

    // when
    ImageText imageText = ImageText.initFrom(imageSegmentation.getDigitalTypes());
    jsonGenericHandler.writeObjectToJsonFile(imageText, new File(OUTPUT));

    // then
    String refText = FileUtils.readFileToString(new File(getClass().getResource("/init-image-text.ref.json").getFile()));
    String actualText = FileUtils.readFileToString(new File(OUTPUT));
    Assertions.assertThat(actualText).isEqualTo(refText);
  }
}
