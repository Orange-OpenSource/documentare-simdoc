package com.orange.documentare.core.model.ref.segmentation;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import org.fest.assertions.Assertions;
import org.junit.Test;

public class DigitalTypesTest {

  @Test
  public void copyWithoutSpaces() {
    // given
    DigitalTypes digitalTypes = new DigitalTypes();
    DigitalType notSpacedigitalType = new DigitalType();
    DigitalType spaceDigitalType = new DigitalType();
    spaceDigitalType.setSpace(true);
    digitalTypes.add(notSpacedigitalType);
    digitalTypes.add(spaceDigitalType);
    digitalTypes.add(notSpacedigitalType);
    digitalTypes.add(spaceDigitalType);
    digitalTypes.add(notSpacedigitalType);

    // when
    DigitalTypes withoutSpaces = digitalTypes.copyWithoutSpaces();

    // then
    for (DigitalType digitalType : withoutSpaces) {
      Assertions.assertThat(digitalType.isSpace()).isFalse();
    }
  }
}
