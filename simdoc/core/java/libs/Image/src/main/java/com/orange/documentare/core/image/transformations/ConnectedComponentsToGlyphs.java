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

import com.orange.documentare.core.image.connectedcomponents.ConnectedComponents;
import com.orange.documentare.core.image.glyphs.Glyphs;

public class ConnectedComponentsToGlyphs {

  /** Connected components have been detected and now it is time to move on with Glyphs */
  public Glyphs transform(ConnectedComponents connectedComponents) {
    DiacriticAndEyeMerger diacriticAndEyeMerger = new DiacriticAndEyeMerger();
    return diacriticAndEyeMerger.buildGlyphs(connectedComponents);
  }
}
