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

import com.orange.documentare.core.model.ref.segmentation.Block;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import com.orange.documentare.core.model.ref.segmentation.SegmentationRect;
import org.apache.commons.io.FileUtils;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;

import java.io.File;
import java.io.IOException;
import java.util.List;

class SegmentationDrawer {
  private Mat imageMat;
  private ImageSegmentation imageSegmentation;
  private File outputImageFile;

  SegmentationDrawer(Segmenter segmenter, File outputImageFile) {
    imageMat = segmenter.getImageMatClone();
    imageSegmentation = segmenter.getImageSegmentation();
    this.outputImageFile = outputImageFile;
  }

  void drawSegmentation() throws IOException {
    drawBlocksSegmentation();
    drawGlyphsSegmentation();
    drawSpaces();
    saveImage();
  }

  private void drawGlyphsSegmentation() {
    Scalar colorRect = new Scalar(255, 0, 0, 0);
    for (SegmentationRect rect : imageSegmentation.getDigitalTypes()) {
      Point tl = new Point(rect.x(), rect.y());
      Point br = new Point(tl.x + rect.width(), tl.y + rect.height());
      Core.rectangle(imageMat, tl, br, colorRect, 1);
    }
  }

  private void drawBlocksSegmentation() {
    Scalar colorLine = new Scalar(0, 0, 255, 0);
    for (Block block : imageSegmentation.getBlocks()) {
      Core.rectangle(imageMat, new Point(block.x(), block.y()-1), new Point(block.x() + block.width(), block.y() + block.height() + 1), colorLine, 1);
    }
  }

  private void drawSpaces() {
    List<DigitalType> digitalTypes = imageSegmentation.getDigitalTypes();
    for (int i = 0; i < digitalTypes.size(); i++) {
      DigitalType digitalType = digitalTypes.get(i);
      if (digitalType.isSpace()) {
        drawSpace(digitalType);
      }
    }
  }

  private void drawSpace(DigitalType r) {
    Scalar colorSpace = new Scalar(64, 64, 64, 0);
    Core.rectangle(imageMat, new Point(r.x() + 1, r.y()), new Point(r.x() + r.width() - 1, r.y() + r.height()), colorSpace, -1);
  }

  private void saveImage() throws IOException {
    MatOfByte matOfByte = new MatOfByte();
    Highgui.imencode(".png", imageMat, matOfByte);
    FileUtils.writeByteArrayToFile(outputImageFile, matOfByte.toArray());
  }
}
