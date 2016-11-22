package com.orange.documentare.core.image.linedetection;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.image.segmentation.posttreatments.RemoveSmallLines;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Lines extends ArrayList<Line> {

  public Lines(Lines lines) {
    super(lines);
  }

  public void removeSmallLines() {
    RemoveSmallLines removeSmallLines = new RemoveSmallLines();
    removeSmallLines.removeSmallLines(this);
  }
}
