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

import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SpaceDetectionTest {

  @Test
  public void addSpacesDigitalTypes() {
    // given
    int digitalTypeSide = 8;
    int digitalTypeLettersGap = 2;
    int digitalTypeSpaceGap = 12;
    int xPosition = 0;
    int nbDigitalTypesInput = 99;
    int expectedSpacesNb = 9;
    Integer[] expectedSpacePositions = { 10, 21, 32, 43, 54, 65, 76, 87, 98 };
    List<Integer> expectedSpacePositionsList = Arrays.asList(expectedSpacePositions);
    DigitalTypes digitalTypes = new DigitalTypes();
    for (int i = 1; i <= nbDigitalTypesInput; i++) {
      DigitalType digitalType = new DigitalType(xPosition, 0, digitalTypeSide, digitalTypeSide);
      digitalTypes.add(digitalType);
      xPosition += digitalTypeSide;
      xPosition += i % 10 == 0 ? digitalTypeSpaceGap : digitalTypeLettersGap;
    }
    SpaceDetection spaceDetection = new SpaceDetection();
    // when
    spaceDetection.detect(digitalTypes);

    // then
    Assertions.assertThat(digitalTypes).hasSize(nbDigitalTypesInput + expectedSpacesNb);
    for (int i = 0; i < (nbDigitalTypesInput + expectedSpacesNb); i++) {
      boolean space = expectedSpacePositionsList.contains(new Integer(i));
      Assertions.assertThat(digitalTypes.get(i).isSpace()).isEqualTo(space);
    }
  }
}
