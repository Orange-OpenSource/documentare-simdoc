package com.orange.documentare.core.comp.multisets;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.FilesAssertThat;
import com.orange.documentare.core.comp.Json;
import com.orange.documentare.core.comp.Res;
import com.orange.documentare.core.model.ref.multisets.MultiSets;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import com.orange.documentare.core.model.ref.text.ImageText;
import org.junit.Test;

import java.io.IOException;

public class MultiSetsBuilderIntegrationTest {

  private static final String OUTPUT_FILENAME = "multisets.json";
  private static final String OUTPUT_BYTES_FILENAME = "multisets_bytes.json";

  private final Res res = new Res(this);
  private final FilesAssertThat filesAssertThat = new FilesAssertThat(this);

  @Test
  public void build() throws IOException {
    // given
    ImageText imageText = (ImageText)
            res.jsonObj("/multisets_text_input.json", ImageText.class);
    MultiSetsBuilder multiSetsBuilder = new MultiSetsBuilder();

    // when
    MultiSets multiSets = multiSetsBuilder.build(imageText);
    Json.toFile(multiSets, OUTPUT_FILENAME);

    // then
    filesAssertThat.theyAreEqual(OUTPUT_FILENAME, "/multisets_text_ref.json");
  }

  @Test
  public void addBytes() throws IOException {
    // given
    MultiSets multiSets = (MultiSets)
            res.jsonObj("/multisets_text_ref.json", MultiSets.class);
    ImageSegmentation imageSegmentation = (ImageSegmentation)
            res.jsonObj("/multisets_image_segmentation_input.json", ImageSegmentation.class);
    MultiSetsBuilder multiSetsBuilder = new MultiSetsBuilder();

    // when
    multiSetsBuilder.addBytes(multiSets, imageSegmentation);
    Json.toFile(multiSets, OUTPUT_BYTES_FILENAME);

    // then
    filesAssertThat.theyAreEqual(OUTPUT_BYTES_FILENAME, "/multisets_bytes_text_ref.json");
  }
}
