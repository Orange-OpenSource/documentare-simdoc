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
import com.orange.documentare.core.model.ref.segmentation.SegmentationRect.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SegmentationCollection {

  private static class AreaComparator implements Comparator<SegmentationRect> {
    @Override
    public int compare(SegmentationRect r1, SegmentationRect r2) {
      return r1.compareAreaTo(r2);
    }
  }

  public static Size getMeanSize(List<? extends SegmentationRect> rects) {
    int widthSum = 0;
    int heightSum = 0;
    for (SegmentationRect rect : rects) {
      widthSum += rect.width();
      heightSum += rect.height();
    }
    return new Size(widthSum / rects.size(), heightSum / rects.size());
  }

  /**
   * @param rects
   * @return Mean Size computed in the range [nb/4 .. nb*3/4] of glyphs ordered by size
   */
  public static Size getCentralMeanSize(List<? extends SegmentationRect> rects) {
    List<? extends SegmentationRect> rectsForMean;
    if (rects.size() < 4) {
      rectsForMean = rects;
    } else {
      rectsForMean = getSizeOrderedRange(rects, 0.25f, 0.75f);
    }
    return getMeanSize(rectsForMean);
  }

  public static List<SegmentationRect> getSizeOrderedRange(List<? extends SegmentationRect> rects, float lowIndexPercent, float highIndexPercent) {
    List<SegmentationRect> sortedRects = new ArrayList<>(rects);
    Collections.sort(sortedRects, new AreaComparator());
    int lowIndex = (int) (lowIndexPercent * sortedRects.size());
    int highIndex = (int) (highIndexPercent * sortedRects.size()) - 1;
    retainAllInRange(sortedRects, lowIndex, highIndex);
    return sortedRects;
  }

  private static void retainAllInRange(List<SegmentationRect> rects, int fromIndex, int toIndex) {
    List<SegmentationRect> toKeep = new ArrayList<>();
    if (fromIndex == toIndex) {
      toKeep.add(rects.get(fromIndex));
    } else {
      toKeep.addAll(rects.subList(fromIndex, toIndex));
    }
    rects.retainAll(toKeep);
  }
}
