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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(suppressConstructorProperties = true)
class SubLines {
  private static float SUBLINE_HEIGHT_TRIGGER_RATIO = 2f;
  private static float SUBLINE_HEIGHT_FORCE_SPLIT_RATIO = 1.5f;

  private final Line line;
  private final ConnectedComponents connectedComponentsBasket;
  private final int meanHeight;
  private final int lineMaxHeight;

  SubLines(Line line) {
    this.line = line;
    ConnectedComponents connectedComponents = line.connectedComponents();
    connectedComponentsBasket = new ConnectedComponents(connectedComponents);
    meanHeight = SegmentationCollection.getMeanSize(connectedComponents).height();
    lineMaxHeight = (int) (line.elemMaxHeight() * SUBLINE_HEIGHT_FORCE_SPLIT_RATIO);
  }

  Lines findSubLines() {
    if (isLineHeightWeird()) {
      return doFindSubLines();
    } else {
      return getLineAsList();
    }
  }

  private boolean isLineHeightWeird() {
    return line.height() > line.elemMaxHeight() * SUBLINE_HEIGHT_TRIGGER_RATIO;
  }

  private Lines doFindSubLines() {
    Lines subLines = getSubLinesFromMediumRects();
    addRestingRectsToSubLines(subLines);
    return subLines;
  }

  private void addRestingRectsToSubLines(Lines subLines) {
    addBigRectsWithOverlapping(subLines);
    refreshSubLinesSize(subLines);
    addRestingRects(subLines);
    refreshSubLinesSize(subLines);
  }

  private Lines getSubLinesFromMediumRects() {
    ConnectedComponents mediumConnectedComponents = getMediumRects();
    connectedComponentsBasket.removeAll(mediumConnectedComponents);
    RawLinesBuilder rawLinesBuilder = new RawLinesBuilder(lineMaxHeight);
    return rawLinesBuilder.build(mediumConnectedComponents);
  }

  private ConnectedComponents getMediumRects() {
    ConnectedComponents mediumRects = new ConnectedComponents();
    for (ConnectedComponent connectedComponent : connectedComponentsBasket) {
      if (isRectMedium(connectedComponent)) {
        mediumRects.add(connectedComponent);
      }
    }
    return mediumRects;
  }

  private boolean isRectMedium(SegmentationRect rect) {
    return !isBigRect(rect) && !isSmallRect(rect);
  }

  private void refreshSubLinesSize(Lines subLines) {
    for (Line subLine : subLines) {
      subLine.refreshSize();
    }
  }

  private void addBigRectsWithOverlapping(Lines subLines) {
    ConnectedComponents connectedComponentsToRemove = new ConnectedComponents();
    for (ConnectedComponent connectedComponent : connectedComponentsBasket) {
      if (isBigRect(connectedComponent)) {
        addBigRectWithOverlapping(connectedComponent, subLines, connectedComponentsToRemove);
      }
    }
    connectedComponentsBasket.removeAll(connectedComponentsToRemove);
  }

  private void addBigRectWithOverlapping(ConnectedComponent connectedComponent, Lines subLines, ConnectedComponents rectsToRemove) {
    boolean added = addRectWithOverlapping(connectedComponent, subLines);
    if (added) {
      rectsToRemove.add(connectedComponent);
    }
  }


  private boolean addRectWithOverlapping(ConnectedComponent connectedComponent, Lines subLines) {
    Lines candidates = new Lines();
    for (Line line : subLines) {
      if (connectedComponent.overlapsOnY(line)) {
        candidates.add(line);
      }
    }
    return addToBestCandidate(connectedComponent, candidates);
  }

 private boolean addToBestCandidate(ConnectedComponent connectedComponent, Lines candidates) {
   if (candidates.isEmpty()) {
     return false;
   } else {
     doAddToBestCandidate(connectedComponent, candidates);
     return true;
   }
  }

  private void doAddToBestCandidate(ConnectedComponent connectedComponent, Lines candidates) {
    if (candidates.size() == 1) {
      candidates.get(0).addButNotGrow(connectedComponent);
    } else {
      Line line1 = candidates.get(0);
      Line line2 = candidates.get(1);
      if (connectedComponent.yOverlap(line1) > connectedComponent.yOverlap(line2)) {
        line1.addButNotGrow(connectedComponent);
      } else {
        line2.addButNotGrow(connectedComponent);
      }
    }
  }

  private void addRestingRects(Lines subLines) {
    ConnectedComponents connectedComponentsToRemove = new ConnectedComponents();
    for (ConnectedComponent connectedComponent : connectedComponentsBasket) {
      boolean added = addRectWithOverlapping(connectedComponent, subLines);
      if (added) {
        connectedComponentsToRemove.add(connectedComponent);
      }
    }
    connectedComponentsBasket.removeAll(connectedComponentsToRemove);
    if (!connectedComponentsBasket.isEmpty()) {
      log.info("Orphan glyphs in lines split detection");
    }
  }

  private Lines getLineAsList() {
    Lines list = new Lines();
    list.add(line);
    return list;
  }


  /* Fixme, use standard deviation, use mean or median ? */

  private boolean isSmallRect(SegmentationRect rect) {
    return rect.height() <= meanHeight * 0.9f;
  }

  private boolean isBigRect(SegmentationRect rect) {
    return rect.height() >= meanHeight * 1.3f;
  }
}
