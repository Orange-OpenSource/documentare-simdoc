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

import com.googlecode.zohhak.api.Coercion;
import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import com.orange.documentare.core.model.ref.multisets.MultiSets;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import com.orange.documentare.core.model.ref.text.*;
import org.fest.assertions.Assertions;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.stream.IntStream;

import static com.orange.documentare.core.comp.multisets.MultiSetsBuilder.CLASS_SIZE_MIN_DEFAULT;

@RunWith(ZohhakRunner.class)
public class MultiSetsBuilderTest {

  private static final String CHAR_1 = "a";
  private static final String CHAR_2 = "b";

  @Coercion
  int stringToInt(String nb) {
    return Integer.parseInt(nb);
  }

  @TestWith({
          "false, 0, 0, 0, 0, 0, 0",
          "false, 10, 0, 0, 0, 10, 0",
          "false, 0, 0, 20, 0, 0, 20",
          "false, 10, 0, 20, 0, 10, 20",
          "false, 10, 5, 0, 0, 10, 0",
          "false, 0, 0, 20, 6, 0, 20",
          "false, 10, 5, 20, 6, 10, 20",
          "true, 10, 5, 0, 0, 15, 0",
          "true, 0, 0, 20, 6, 0, 26",
          "true, 10, 5, 20, 6, 15, 26",
  })
  public void buildMultisetsWithDefaultClassSizeRestriction(boolean includePropagatedChars, int nbOfChar1, int nbOfPropagatedChar1, int nbOfChar2, int nbOfPropagatedChar2, int multiset1ExpectedSize, int multiset2ExpectedSize) throws IOException {
    // given
    ImageText imageText = new ImageText();
    imageText.setTextElements(buildTextElementsWithChars(CHAR_1, nbOfChar1, nbOfPropagatedChar1, CHAR_2, nbOfChar2, nbOfPropagatedChar2));
    MultiSetsBuilder multiSetsBuilder = new MultiSetsBuilder(includePropagatedChars, CLASS_SIZE_MIN_DEFAULT);

    // when
    MultiSets multiSets = multiSetsBuilder.build(imageText);

    // then
    if (multiset1ExpectedSize == 0 && multiset2ExpectedSize == 0) {
      Assertions.assertThat(multiSets).isEmpty();
    } else if (multiset1ExpectedSize != 0 && multiset2ExpectedSize == 0) {
      Assertions.assertThat(multiSets).hasSize(1);
      Assertions.assertThat(multiSets.get(0).getDigitalTypeIndices()).hasSize(multiset1ExpectedSize);
    } else if (multiset1ExpectedSize == 0 && multiset2ExpectedSize != 0) {
      Assertions.assertThat(multiSets).hasSize(1);
      Assertions.assertThat(multiSets.get(0).getDigitalTypeIndices()).hasSize(multiset2ExpectedSize);
    } else {
      Assertions.assertThat(multiSets).hasSize(2);
      Assertions.assertThat(multiSets.get(0).getDigitalTypeIndices()).hasSize(multiset1ExpectedSize);
      Assertions.assertThat(multiSets.get(1).getDigitalTypeIndices()).hasSize(multiset2ExpectedSize);
    }
  }


  @TestWith({
          "false, 0, 0, 0, 0, 0, 0",
          "false, 10, 0, 0, 0, 0, 0",
          "false, 0, 0, 20, 0, 0, 14",
          "false, 10, 0, 20, 0, 0, 14",
          "false, 10, 5, 0, 0, 0, 0",
          "false, 0, 0, 20, 6, 0, 14",
          "false, 10, 5, 20, 6, 0, 14",
          "true, 10, 5, 0, 0, 14, 0",
          "true, 0, 0, 20, 6, 0, 14",
          "true, 10, 5, 20, 6, 14, 14",
  })
  public void buildMultisetsWithClassSizeRestrictionAndNoCenters(boolean includePropagatedChars, int nbOfChar1, int nbOfPropagatedChar1, int nbOfChar2, int nbOfPropagatedChar2, int multiset1ExpectedSize, int multiset2ExpectedSize) throws IOException {
    // given
    int classSizeMin = 11;
    int classSizeMax = 14;
    ImageText imageText = new ImageText();
    imageText.setTextElements(buildTextElementsWithChars(CHAR_1, nbOfChar1, nbOfPropagatedChar1, CHAR_2, nbOfChar2, nbOfPropagatedChar2));
    MultiSetsBuilder multiSetsBuilder = new MultiSetsBuilder(includePropagatedChars, classSizeMin);

    // when
    MultiSets multiSets = multiSetsBuilder.build(imageText);
    multiSetsBuilder.limitClassSize(multiSets, buildImageSegmentation(nbOfChar1 + nbOfPropagatedChar1 + nbOfChar2 + nbOfPropagatedChar2), classSizeMax);

    // then
    if (multiset1ExpectedSize == 0 && multiset2ExpectedSize == 0) {
      Assertions.assertThat(multiSets).isEmpty();
    } else if (multiset1ExpectedSize != 0 && multiset2ExpectedSize == 0) {
      Assertions.assertThat(multiSets).hasSize(1);
      Assertions.assertThat(multiSets.get(0).getDigitalTypeIndices()).hasSize(multiset1ExpectedSize);
    } else if (multiset1ExpectedSize == 0 && multiset2ExpectedSize != 0) {
      Assertions.assertThat(multiSets).hasSize(1);
      Assertions.assertThat(multiSets.get(0).getDigitalTypeIndices()).hasSize(multiset2ExpectedSize);
    } else {
      Assertions.assertThat(multiSets).hasSize(2);
      Assertions.assertThat(multiSets.get(0).getDigitalTypeIndices()).hasSize(multiset1ExpectedSize);
      Assertions.assertThat(multiSets.get(1).getDigitalTypeIndices()).hasSize(multiset2ExpectedSize);
    }
  }

  @TestWith("10, 5, 20, 6")
  public void checkMultisetsIndices(int nbOfChar1, int nbOfPropagatedChar1, int nbOfChar2, int nbOfPropagatedChar2) throws IOException {
    // given
    ImageText imageText = new ImageText();
    imageText.setTextElements(buildTextElementsWithChars(CHAR_1, nbOfChar1, nbOfPropagatedChar1, CHAR_2, nbOfChar2, nbOfPropagatedChar2));
    MultiSetsBuilder multiSetsBuilder = new MultiSetsBuilder(true, CLASS_SIZE_MIN_DEFAULT);

    // when
    MultiSets multiSets = multiSetsBuilder.build(imageText);
    DigitalTypeIndices digitalTypeIndices1 = multiSets.get(0).getDigitalTypeIndices();
    DigitalTypeIndices digitalTypeIndices2 = multiSets.get(1).getDigitalTypeIndices();

    // then
    IntStream.range(0, nbOfChar1 + nbOfPropagatedChar1).forEach(i ->
            Assertions.assertThat(digitalTypeIndices1.get(i)).isEqualTo(i)
    );
    IntStream.range(0, nbOfChar2 + nbOfPropagatedChar2).forEach(i ->
            Assertions.assertThat(digitalTypeIndices2.get(i)).isEqualTo(i + nbOfChar1 + nbOfPropagatedChar1)
    );
  }

  @TestWith({
          "true, 1, 1, 5, 5, 2, 3",
  })
  public void limitMultisetsWithClusterCenters(boolean includePropagatedChars, int nbOfChar1, int nbOfPropagatedChar1, int nbOfChar2, int nbOfPropagatedChar2, int multiset1ExpectedSize, int multiset2ExpectedSize) throws IOException {
    // given
    int classSizeMin = 1;
    int classSizeMax = 3;
    ImageText imageText = new ImageText();
    imageText.setTextElements(buildTextElementsWithChars(CHAR_1, nbOfChar1, nbOfPropagatedChar1, CHAR_2, nbOfChar2, nbOfPropagatedChar2));
    MultiSetsBuilder multiSetsBuilder = new MultiSetsBuilder(includePropagatedChars, classSizeMin);
    ImageSegmentation imageSegmentation = buildImageSegmentation(nbOfChar1 + nbOfPropagatedChar1 + nbOfChar2 + nbOfPropagatedChar2);
    DigitalTypes digitalTypes = imageSegmentation.getDigitalTypes();
    digitalTypes.get(0).setClusterCenter(true);
    digitalTypes.get(1).setClusterCenter(true);
    digitalTypes.get(nbOfChar1 + nbOfPropagatedChar1).setClusterCenter(true);
    digitalTypes.get(nbOfChar1 + nbOfPropagatedChar1 + 1).setClusterCenter(true);

    // when
    MultiSets multiSets = multiSetsBuilder.build(imageText);
    multiSetsBuilder.limitClassSize(multiSets, imageSegmentation, classSizeMax);

    // then
    DigitalTypeIndices digitalTypeIndicesM1 = multiSets.get(0).getDigitalTypeIndices();
    DigitalTypeIndices digitalTypeIndicesM2 = multiSets.get(1).getDigitalTypeIndices();

    Assertions.assertThat(multiSets).hasSize(2);
    Assertions.assertThat(digitalTypeIndicesM1).hasSize(multiset1ExpectedSize);
    Assertions.assertThat(digitalTypeIndicesM2).hasSize(multiset2ExpectedSize);
    Assertions.assertThat(digitalTypeIndicesM1.stream()
            .filter(index -> digitalTypes.get(index).isClusterCenter())
            .count()).isEqualTo(2);
    Assertions.assertThat(digitalTypeIndicesM2.stream()
            .filter(index -> digitalTypes.get(index).isClusterCenter())
            .count()).isEqualTo(2);
  }

  private ImageSegmentation buildImageSegmentation(int nbOfDigitalTypes) {
    ImageSegmentation imageSegmentation = new ImageSegmentation();
    DigitalTypes digitalTypes = imageSegmentation.getDigitalTypes();
    IntStream.range(0, nbOfDigitalTypes).forEach(i -> digitalTypes.add(new DigitalType()));
    return imageSegmentation;
  }

  private TextElements buildTextElementsWithChars(String char1, int nbOfChar1, int nbOfPropagatedChar1, String char2, int nbOfChar2, int nbOfPropagatedChar2) {
    TextElements textElements = new TextElements();
    addChar(0, textElements, char1, nbOfChar1, nbOfPropagatedChar1);
    addChar(nbOfChar1 + nbOfPropagatedChar1, textElements, char2, nbOfChar2, nbOfPropagatedChar2);
    return textElements;
  }

  private void addChar(int indexOffset, TextElements textElements, String character, int nbOfChar, int nbOfPropagatedChar) {
    IntStream.range(indexOffset, indexOffset + nbOfChar)
            .forEach(i -> addChar(i, textElements, character, true));
    IntStream.range(indexOffset + nbOfChar, indexOffset + nbOfChar + nbOfPropagatedChar)
            .forEach(i -> addChar(i, textElements, character, false));
  }

  private void addChar(int i, TextElements textElements, String character, boolean userModified) {
    DigitalTypeIndices digitalTypeIndices = new DigitalTypeIndices();
    digitalTypeIndices.add(i);
    textElements.add(new TextElement(TextElementType.C, digitalTypeIndices, character, userModified, false));
  }
}
