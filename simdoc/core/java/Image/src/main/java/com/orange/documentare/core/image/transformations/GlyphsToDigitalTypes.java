package com.orange.documentare.core.image.transformations;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.image.glyphs.Glyph;
import com.orange.documentare.core.image.glyphs.Glyphs;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;

/** Build Digital Types from Glyphs: ie. extends glyphs' height to match line height */
public class GlyphsToDigitalTypes {

  public DigitalTypes transform(Glyphs glyphs, int lineY, int lineHeight) {
    DigitalTypes digitalTypes = new DigitalTypes();
    for (Glyph glyph : glyphs) {
      digitalTypes.add(new DigitalType(glyph.x(), lineY, glyph.width(), lineHeight));
    }
    return digitalTypes;
  }
}
