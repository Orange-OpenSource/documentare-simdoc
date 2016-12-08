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

import com.orange.documentare.core.image.connectedcomponents.ConnectedComponents;
import com.orange.documentare.core.image.segmentation.SegmentationDebug;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class LineDetection {
  private SegmentationDebug debug = new SegmentationDebug(null);

  public Lines detectLines(ConnectedComponents connectedComponents) {
    LinesGroup linesGroup = new LinesGroup();
    linesGroup.add(buildLines(connectedComponents, false));
    doAnalysisUntilStableState(linesGroup, false);
    PostTreatments postTreatments = new PostTreatments(this, debug);
    Lines lines = postTreatments.applyOn(linesGroup);
    debug.lines(lines);
    return lines;
  }

  void doAnalysisUntilStableState(LinesGroup linesGroup, boolean alienDetection) {
    Lines lines = linesGroup.asFlattenLines();
    int linesCount;
    do {
      debug.lines(lines);
      linesCount = lines.size();
      linesGroup.clear();
      doAnalysisWithLoopOnLines(linesGroup, lines, alienDetection);
      lines = linesGroup.asFlattenLines();
    } while(lines.size() != linesCount);
  }

  private void doAnalysisWithLoopOnLines(List<Lines> allLines, Lines lines, boolean alienDetection) {
    for (Line line : lines) {
      allLines.add(buildLines(line.connectedComponents(), alienDetection));
    }
  }

  private Lines buildLines(ConnectedComponents connectedComponents, boolean alienDetection) {
    RawLinesBuilder rawLinesBuilder = new RawLinesBuilder(Integer.MAX_VALUE);
    SubColumnsBuilder subColumnsBuilder = new SubColumnsBuilder();
    Lines rawLines = rawLinesBuilder.build(connectedComponents);
    debug.rawLines(rawLines);
    return subColumnsBuilder.build(rawLines, alienDetection);
  }
}
