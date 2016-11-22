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
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
public class OuterRectBuilder {
  @Getter
  private int xMin, yMin;

  private int xMax, yMax;

  @Getter
  private int elemMaxHeight;

  public OuterRectBuilder() {
    reset();
  }

  public void reset() {
    xMin = yMin = Integer.MAX_VALUE;
    xMax = yMax = elemMaxHeight = 0;
  }

  public void buildFor(List<? extends SegmentationRect> rects) {
    reset();
    for (SegmentationRect rect : rects) {
      add(rect);
    }
  }

  public void add(SegmentationRect r) {
    xMin = Math.min(xMin, r.x());
    yMin = Math.min(yMin, r.y());
    xMax = Math.max(xMax, r.x() + r.width());
    yMax = Math.max(yMax, r.y() + r.height());
    elemMaxHeight = Math.max(elemMaxHeight, r.height());
  }

  public int width() {
    return xMax - xMin;
  }

  public int height() {
    return yMax - yMin;
  }

  public boolean isAboveYMax(SegmentationRect r) {
      return r.y() < yMax;
  }
}
