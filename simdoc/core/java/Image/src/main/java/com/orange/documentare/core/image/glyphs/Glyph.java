package com.orange.documentare.core.image.glyphs;
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

/**
 * Image glyph, ie the aggregation of connected components to match the image of a character, including
 * diacritics, character eye, etc
 */
public class Glyph extends ConnectedComponent {
  public Glyph(ConnectedComponent connectedComponent) {
    super(connectedComponent);
  }
}
