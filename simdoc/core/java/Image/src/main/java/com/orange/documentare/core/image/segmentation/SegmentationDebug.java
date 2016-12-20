package com.orange.documentare.core.image.segmentation;
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
import com.orange.documentare.core.image.glyphs.Glyphs;
import com.orange.documentare.core.image.linedetection.Lines;
import com.orange.documentare.core.image.test.TestDrawer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class SegmentationDebug {
  @Getter
  private final File imageFile;

  @Setter
  private boolean debugEnabled;

  private Glyphs glyphs = new Glyphs();

  private int step;

  public int newStep() {
    return step++;
  }

  private boolean debugEnabledFirstRawLinesDone;

  public void cc(ConnectedComponents connectedComponents) {
    if (debugEnabled) {
      try {
        newStep();
        TestDrawer.draw(imageFile, new File("ld_debug_0_cc.png"), new Lines(), connectedComponents);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void rawLines(Lines rawLines) {
    if (!debugEnabledFirstRawLinesDone) {
      if (debugEnabled) {
        newStep();
        try {
          TestDrawer.drawLines(imageFile, new File("ld_debug_1_raw_lines.png"), rawLines);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      debugEnabledFirstRawLinesDone = true;
      log("\tExtract raw lines from connected components");
    }
  }

  public void lines(Lines lines) {
    log("\tFind sublines and subcolumns loop, lines count: " + lines.size());
    if (debugEnabled) {
      try {
        TestDrawer.drawLines(imageFile, new File("ld_debug_" + newStep() + "_linedetection.png"), lines);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void clearGlyphs() {
    glyphs.clear();
  }

  public void addGlyphs(Glyphs glyphs) {
    if (debugEnabled) {
      this.glyphs.addAll(glyphs);
    }
  }

  public void saveGlyphs(Lines lines) {
    if (debugEnabled) {
      try {
        TestDrawer.draw(imageFile, new File("ld_debug_" + newStep() + "_glyphs.png"), lines, glyphs);
        glyphs.clear();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void log(String message) {
    log.info(message);
  }
}
