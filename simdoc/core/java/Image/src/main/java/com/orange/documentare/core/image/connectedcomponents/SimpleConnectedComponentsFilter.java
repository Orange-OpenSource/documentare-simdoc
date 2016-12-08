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

import com.orange.documentare.core.image.segmentationcollection.SegmentationCollection;
import com.orange.documentare.core.model.ref.segmentation.SegmentationRect.Size;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
/** Remove CC which are really too big compared to mean CC */
class SimpleConnectedComponentsFilter implements ConnectedComponentsDetector.ConnectedComponentsFilter {
  private static final int RATIO = 40;
  private final int imageWidth;

  @Override
  public ConnectedComponents filter(ConnectedComponents connectedComponents) {
    ConnectedComponents toRemove = new ConnectedComponents();
    Size mean = SegmentationCollection.getCentralMeanSize(connectedComponents);
    for (ConnectedComponent connectedComponent : connectedComponents) {
      if (isTooBig(connectedComponent, mean)) {
        toRemove.add(connectedComponent);
      }
    }
    return toRemove;
  }

  private boolean isTooBig(ConnectedComponent cc, Size mean) {
    boolean invalidHeight = cc.height() > mean.width() * RATIO;
    boolean invalidWidth = cc.width() > mean.height() * RATIO;
    invalidWidth = cc.width() > imageWidth / 2 || invalidWidth;
    return invalidHeight || invalidWidth;
  }
}
