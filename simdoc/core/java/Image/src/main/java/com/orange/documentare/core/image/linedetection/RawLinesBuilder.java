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
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class RawLinesBuilder {
  private final int lineMaxHeight;

  Lines build(ConnectedComponents connectedComponents) {
    connectedComponents.sortVertically();
    Lines rawLines = new Lines();
    rawLines.add(new Line());
    for (ConnectedComponent connectedComponent : connectedComponents) {
      addRectToALine(rawLines, connectedComponent);
    }
    removeEmptyLines(rawLines);
    return rawLines;
  }

  private void addRectToALine(Lines lines, ConnectedComponent connectedComponent) {
    Line lastLine = lines.get(lines.size()-1);
    if (lastLine.couldContain(connectedComponent, lineMaxHeight)) {
      lastLine.add(connectedComponent);
    } else {
      createNewLineFromRect(lines, connectedComponent);
    }
  }

  private void removeEmptyLines(Lines lines) {
    Lines toRemove = new Lines();
    for (Line line : lines) {
      if (line.connectedComponents().isEmpty()) {
        toRemove.add(line);
      }
    }
    lines.removeAll(toRemove);
  }

  private void createNewLineFromRect(Lines lines, ConnectedComponent connectedComponent) {
    Line newLine = new Line();
    newLine.add(connectedComponent);
    lines.add(newLine);
  }
}
