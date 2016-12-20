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

public class SubColumnsBuilder {

  Lines build(Lines lines, boolean alienDetection) {
    Lines linesWithSubColumns = new Lines();
    for (Line line : lines) {
      if (!line.connectedComponents().isEmpty()) {
        linesWithSubColumns.addAll(line.findSubColumns(alienDetection));
      }
    }
    return linesWithSubColumns;
  }
}
