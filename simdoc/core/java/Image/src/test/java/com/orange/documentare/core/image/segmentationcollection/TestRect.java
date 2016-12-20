package com.orange.documentare.core.image.segmentationcollection;
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

class TestRect extends SegmentationRect {
  TestRect(int x, int y, int width, int height) {
    super(x, y, width, height);
  }
}
