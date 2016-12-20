package com.orange.documentare.core.image.segmentation.posttreatments;
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
import com.orange.documentare.core.image.linedetection.Line;
import com.orange.documentare.core.image.segmentationcollection.OuterRectBuilder;
import com.orange.documentare.core.image.segmentationcollection.SegmentationCollection;
import com.orange.documentare.core.model.ref.segmentation.SegmentationRect.Size;

public class LineNoiseReduction {
  private static final float NOISE_RATIO = 3.5f;

  public void removeNoise(Line line, Glyphs glyphs) {
    Size meanSize = SegmentationCollection.getCentralMeanSize(glyphs);
    Glyphs toRemove = new Glyphs();
    for(Glyph glyph : glyphs) {
      if (glyph.isNoise(meanSize, NOISE_RATIO)) {
        toRemove.add(glyph);
      }
    }
    glyphs.removeAll(toRemove);
    updateLineGeometry(line, glyphs);
  }

  private void updateLineGeometry(Line line, Glyphs glyphs) {
    OuterRectBuilder outer = new OuterRectBuilder();
    outer.buildFor(glyphs);
    line.setGeometry(outer.xMin(), outer.yMin(), outer.width(), outer.height());
  }
}
