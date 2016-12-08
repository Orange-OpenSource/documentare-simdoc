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

import com.orange.documentare.core.image.connectedcomponents.ConnectedComponent;
import com.orange.documentare.core.image.connectedcomponents.ConnectedComponents;
import com.orange.documentare.core.image.segmentationcollection.SegmentationCollection;
import com.orange.documentare.core.model.ref.segmentation.SegmentationRect;
import com.orange.documentare.core.model.ref.segmentation.SegmentationRect.Size;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(suppressConstructorProperties = true)
class SubColumns {
  private static final int ALIEN_VERTICAL_AXIS_RATIO = 4;
  private static final int SUBLINE_RATIO = 4;

  private final Line line;
  private final boolean alienDetection;

  Lines findSubColumns() {
    if (line.connectedComponents().size() == 1) {
      return subColumnForSingleRect();
    } else {
      return subColumns(alienDetection);
    }
  }

  private Lines subColumnForSingleRect() {
    Lines subList = new Lines();
    subList.add(line);
    return subList;
  }

  private Lines subColumns(boolean alienDetection) {
    ConnectedComponents connectedComponents = line.connectedComponents();
    connectedComponents.mergeOverlapped();
    return doFindSubColumns(alienDetection);
  }

  private Lines doFindSubColumns(boolean alienDetection) {
    Lines subColumns = new Lines();
    loopOnRectsForSubColumn(subColumns, alienDetection, SegmentationCollection.getMeanSize(line.connectedComponents()));
    return subColumns;
  }

  private void loopOnRectsForSubColumn(Lines subColumns, boolean alienDetection, Size meanSize) {
    ConnectedComponents connectedComponents = line.connectedComponents();
    int sumWidth = 0;
    Line lastLine = new Line();
    subColumns.add(lastLine);
    for(int i = 0; i < connectedComponents.size()-1; i++) {
      sumWidth += connectedComponents.get(i).width();
      boolean newSubLine = subColumnRectsAnalysis(i, subColumns, alienDetection, meanSize, sumWidth);
      sumWidth = newSubLine ? 0 : sumWidth;
    }
  }

  private boolean subColumnRectsAnalysis(int i, Lines subColumns, boolean alienDetection, Size meanSize, int sumWidth) {
    ConnectedComponents connectedComponents = line.connectedComponents();
    boolean newSubColumn = false;
    ConnectedComponent rect = connectedComponents.get(i);
    ConnectedComponent next = connectedComponents.get(i+1);
    Line lastLine = subColumns.get(subColumns.size()-1);
    lastLine.add(rect);
    sumWidth += rect.width();

    /* FIXME: we should use space between chars and not the mean setWidth */
    boolean nextIsTooFar = rect.xGapTo(next) > (SUBLINE_RATIO * sumWidth) / lastLine.connectedComponents().size();

    boolean alienDetected = isAlien(rect, meanSize) || isAlien(next, meanSize);
    if (nextIsTooFar || (alienDetection && alienDetected)) {
      lastLine = new Line();
      subColumns.add(lastLine);
      newSubColumn = true;
    }
    if (i == connectedComponents.size() - 2) {
      lastLine.add(next);
    }
    return newSubColumn;
  }

  private boolean isAlien(SegmentationRect rect, Size meanSize) {
    /* FIXME: consider a statistical approach : thresh = meanHeight + n  * standard deviation */
    return rect.height() > meanSize.height() * ALIEN_VERTICAL_AXIS_RATIO;
  }
}
