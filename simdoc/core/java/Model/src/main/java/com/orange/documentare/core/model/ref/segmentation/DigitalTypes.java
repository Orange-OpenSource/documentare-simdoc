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

import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
public class DigitalTypes extends ArrayList<DigitalType> {
  public DigitalTypes(DigitalTypes digitalTypes) {
    super(digitalTypes);
  }

  /** @return Digital type for the space/gap between items index - 1 and index */
  public DigitalType buildSpaceDigitalTypeBefore(int index) {
    DigitalType digitalTypeAtIndex = get(index);
    DigitalType previousDigitalType = get(index - 1);
    int spaceX = previousDigitalType.x() + previousDigitalType.width();
    int spaceWidth = digitalTypeAtIndex.x() - spaceX;
    DigitalType spaceDigitalType = new DigitalType(spaceX, digitalTypeAtIndex.y(), spaceWidth, digitalTypeAtIndex.height());
    spaceDigitalType.setSpace(true);
    return spaceDigitalType;
  }

  public DigitalTypes copyWithoutSpaces() {
    DigitalTypes digitalTypes = new DigitalTypes();
    for (DigitalType digitalType : this) {
      if (!digitalType.isSpace()) {
        digitalTypes.add(digitalType);
      }
    }
    return digitalTypes;
  }

  /** Remove distances and image bytes */
  public void strip() {
    for (DigitalType digitalType : this) {
      digitalType.strip();
    }
  }
}
