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
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ImageTextIntegrationTest {

  @Test
  public void shouldImportImageText() throws IOException {
    // given
    String refFileResource = "/text.json";
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
    File ref = new File(getClass().getResource(refFileResource).getFile());

    // when
    ImageText imageText = (ImageText) jsonGenericHandler.getObjectFromJsonFile(ImageText.class, ref);

    // then
    TextElement el = imageText.getTextElements().get(22);
    Assertions.assertThat(el.getType()).isEqualTo(TextElementType.C);
    Assertions.assertThat(el.getChars()).isEqualTo("al");
    Assertions.assertThat(el.getDigitalTypeIndices().get(0)).isEqualTo(22);
    Assertions.assertThat(el.userModified()).isEqualTo(true);
  }
}
