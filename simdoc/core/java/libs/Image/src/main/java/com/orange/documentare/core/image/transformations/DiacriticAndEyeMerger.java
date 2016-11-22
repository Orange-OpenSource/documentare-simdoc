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

import com.orange.documentare.core.image.connectedcomponents.ConnectedComponent;
import com.orange.documentare.core.image.connectedcomponents.ConnectedComponents;
import com.orange.documentare.core.image.glyphs.Glyph;
import com.orange.documentare.core.image.glyphs.Glyphs;

class DiacriticAndEyeMerger {
  public Glyphs buildGlyphs(ConnectedComponents connectedComponents) {
    connectedComponents.mergeDiacriticsAndEyes();
    Glyphs glyphs = new Glyphs();
    for (ConnectedComponent connectedComponent : connectedComponents) {
      glyphs.add(new Glyph(connectedComponent));
    }
    return glyphs;
  }
}
