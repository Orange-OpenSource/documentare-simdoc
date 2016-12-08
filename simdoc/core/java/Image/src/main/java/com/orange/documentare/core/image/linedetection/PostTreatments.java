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

import com.orange.documentare.core.image.segmentation.SegmentationDebug;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(suppressConstructorProperties = true)
class PostTreatments {
  private static final boolean ALIEN_DETECTION = true;
  private static final boolean DETECT_MERGED_LINES = true;

  private final LineDetection lineDetection;
  private final SegmentationDebug debug;

  Lines applyOn(LinesGroup linesGroup) {
    if (ALIEN_DETECTION) {
      debug.log("\tTry to find alien lines: connected components really too big compared to the rest");
      lineDetection.doAnalysisUntilStableState(linesGroup, true);
    }
    Lines lines = linesGroup.asFlattenLines();
    if (DETECT_MERGED_LINES) {
      debug.log("\tTry to find overlapped lines which need to be split");
      lines = splitMergedLines(lines);
    }
    return lines;
  }

  private Lines splitMergedLines(Lines lines) {
    Lines withSubLines = new Lines();
    for (Line line : lines) {
      withSubLines.addAll(getLineWithSubLines(line));
    }
    return withSubLines;
  }

  private Lines getLineWithSubLines(Line line) {
    Lines withSubLines = new Lines();
    createSubLinesFrom(withSubLines, line);
    return withSubLines;
  }

  private void createSubLinesFrom(Lines withSubLines, Line line) {
    if (!line.connectedComponents().isEmpty()) {
      Lines subLines = line.findSubLines();
      withSubLines.addAll(subLines);
    }
  }
}
