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

import com.orange.documentare.core.model.ref.segmentation.SegmentationRect;

class DiacriticAndEyeOverlap implements Overlap {

  @Override
  public boolean areOverlapping(SegmentationRect r1, SegmentationRect r2) {
    return r1.overlaps(r2) || r2.isADiacriticOf(r1);
  }
}
