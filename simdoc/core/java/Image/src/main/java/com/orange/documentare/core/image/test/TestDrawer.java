package com.orange.documentare.core.image.test;
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
import com.orange.documentare.core.image.linedetection.Line;
import com.orange.documentare.core.image.linedetection.Lines;
import com.orange.documentare.core.image.opencv.OpenCvImage;
import com.orange.documentare.core.image.opencv.OpencvLoader;
import com.orange.documentare.core.model.ref.segmentation.SegmentationRect;
import org.apache.commons.io.FileUtils;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestDrawer {

  private static final Scalar ccCol = new Scalar(255, 0, 0, 0);
  private static final Scalar linesCol = new Scalar(0, 0, 255, 0);

  public static void drawLines(File inputFile, File outputFile, Lines lines) throws IOException {
    ConnectedComponents connectedComponents = new ConnectedComponents();
    for (Line line : lines) {
      connectedComponents.addAll(line.connectedComponents());
    }
    draw(inputFile, outputFile, lines, connectedComponents);
  }

  public static void draw(File inputFile, File outputFile, Lines lines, List<? extends SegmentationRect> rects) throws IOException {
    Mat imageMat = OpenCvImage.getMat(inputFile);
    for (SegmentationRect r : lines) {
      Core.rectangle(imageMat, new Point(r.x(), r.y() - 1), new Point(r.x() + r.width(), r.y() + r.height() + 1), linesCol, 1);
    }
    for (SegmentationRect r : rects) {
        Core.rectangle(imageMat, new Point(r.x(), r.y()), new Point(r.x() + r.width(), r.y() + r.height()), ccCol, 1);
    }
    saveImage(imageMat, outputFile);
  }

  private static void saveImage(Mat imageMat, File outputFile) throws IOException {
    MatOfByte matOfByte = new MatOfByte();
    Highgui.imencode(".png", imageMat, matOfByte);
    FileUtils.writeByteArrayToFile(outputFile, matOfByte.toArray());
  }
}
