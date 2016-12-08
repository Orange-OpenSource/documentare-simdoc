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

public class TestRect extends SegmentationRect {
  public TestRect(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  public TestRect(SegmentationRect r) {
    setGeometry(r);
  }
}
