package com.orange.documentare.core.comp.multisets.reco;
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
import com.orange.documentare.core.model.ref.multisets.DigitalTypesClasses;
import com.orange.documentare.core.model.ref.multisets.MultiSets;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import com.orange.documentare.core.model.ref.text.ImageText;
import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class OCRTest {

  private static final String OUTPUT_OCR_CLASSES = "ocr_classes.json";
  private static final String OUTPUT_OCR_IMAGE_TEXT = "ocr_image_text.json";

  private final Res res = new Res(this);
  private final FilesAssertThat filesAssertThat = new FilesAssertThat(this);

  @After
  public void cleanup() {
    FileUtils.deleteQuietly(new File(OUTPUT_OCR_CLASSES));
    FileUtils.deleteQuietly(new File(OUTPUT_OCR_IMAGE_TEXT));
  }

  @Test
  public void ocr() throws IOException {
    // given
    MultiSets multiSets = (MultiSets)
            res.jsonObj("/ocr_multisets_input.json", MultiSets.class);
    ImageSegmentation segInput = (ImageSegmentation)
            res.jsonObj("/ocr_image_segmentation_input.json", ImageSegmentation.class);
    // do
    OCR ocr = new OCR(multiSets);
    DigitalTypesClasses digitalTypesClasses = ocr.doRecoOn(segInput.getDigitalTypes());

    // then
    Json.toFile(digitalTypesClasses, OUTPUT_OCR_CLASSES);
    filesAssertThat.theyAreEqual(OUTPUT_OCR_CLASSES, "/ocr_classes_ref.json");

    // Make sure we cache the compressed length for optimization purpose
    ocr.multiSetNcdInputs.stream().forEach( multisetNcdInput ->
      Assertions.assertThat(multisetNcdInput.compressedLengthAvailable).isTrue()
    );
  }

  @Test
  public void ocrImageText() throws IOException {
    // given
    MultiSets multiSets = (MultiSets)
            res.jsonObj("/ocr_multisets_input.json", MultiSets.class);
    ImageSegmentation segInput = (ImageSegmentation)
            res.jsonObj("/ocr_image_segmentation_input.json", ImageSegmentation.class);
    // do
    OCR ocr = new OCR(multiSets);
    DigitalTypesClasses digitalTypesClasses = ocr.doRecoOn(segInput.getDigitalTypes());
    ImageText imageText = ocr.buildImageText(segInput.getDigitalTypes(), digitalTypesClasses);

    // then
    Json.toFile(imageText, OUTPUT_OCR_IMAGE_TEXT);
    filesAssertThat.theyAreEqual(OUTPUT_OCR_IMAGE_TEXT, "/ocr_image_text.ref.json");
  }
}
