package com.orange.documentare.core.image.segmentation.posttreatments;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.image.linedetection.Line;
import com.orange.documentare.core.image.linedetection.Lines;

import java.util.Collections;
import java.util.Comparator;

public class RemoveSmallLines {
  private static final int SMALL_LINE_RATIO = 2;

  private class LineHeightComparator implements Comparator<Line> {
    @Override
    public int compare(Line l1, Line l2) {
      return Integer.compare(l1.height(), l2.height());
    }
  }

  public void removeSmallLines(Lines lines) {
    Lines linesToRemove = new Lines();
    int linesMeanHeight = getLinesMeanHeight(lines);
    for (Line line : lines) {
      if (line.height() < linesMeanHeight/SMALL_LINE_RATIO) {
        linesToRemove.add(line);
      }
    }
    lines.removeAll(linesToRemove);
  }

  private int getLinesMeanHeight(Lines lines) {
    Lines copyLines = new Lines(lines);
    Collections.sort(copyLines, new LineHeightComparator());
    int sum = 0;
    int nb = 0;
    for (int i = copyLines.size()/2; i < copyLines.size(); i++ ) {
      Line line = copyLines.get(i);
      if (line.height() > 0) {
        sum += line.height();
        nb++;
      }
    }
    return sum/nb;
  }
}
