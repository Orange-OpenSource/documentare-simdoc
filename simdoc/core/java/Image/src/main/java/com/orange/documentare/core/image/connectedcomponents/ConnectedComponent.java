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
import lombok.NoArgsConstructor;

/**
 * We use an image library to analyse the image and provide us "contours" of shapes in the image.
 * These "contours" will match the geometry of images of characters, but at a lower grain: will also
 * include eyes, diacritics, etc.
 * So post processing will be necessary to regroup some connected components to produce Glyphs.
 */
@NoArgsConstructor
public class ConnectedComponent extends SegmentationRect {

  protected ConnectedComponent(ConnectedComponent cc) {
    super(cc.x, cc.y, cc.width, cc.height);
  }

  public ConnectedComponent(int x, int y, int width, int height) {
    super(x, y, width, height);
  }
}
