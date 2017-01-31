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

import com.orange.documentare.core.image.Binarization;
import com.orange.documentare.core.image.connectedcomponents.ConnectedComponents;
import com.orange.documentare.core.image.connectedcomponents.ConnectedComponentsDetector;
import com.orange.documentare.core.image.linedetection.LineDetection;
import com.orange.documentare.core.image.linedetection.Lines;
import com.orange.documentare.core.image.opencv.OpenCvImage;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import lombok.Getter;
import lombok.Setter;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;

public class Segmenter {
  @Getter
  private final File imageFile;

  /** opencv image matrix */
  private final Mat imageMat;
  /** opencv binarized image matrix */
  private final Mat binaryImageMat;

  private final SegmentationDebug debug;

  @Setter
  private boolean cleanSmallLines = true;

  @Setter
  /** whether to embed in the segmentation dat the cropped binary image bytes */
  private boolean embedCrop = false;

  @Getter
  private ImageSegmentation imageSegmentation;

  public Segmenter(File imageFile) {
    this.imageFile = imageFile;
    debug = new SegmentationDebug(imageFile);
    debug.log("Load image");
    imageMat = OpenCvImage.loadMat(imageFile);
    debug.log("Binarize image");
    binaryImageMat = Binarization.getFrom(imageMat);
  }

  public void enableDebug() {
    debug.setDebugEnabled(true);
  }

  public void doSegmentation() {
    ConnectedComponentsDetector detector = new ConnectedComponentsDetector();
    LineDetection lineDetection = new LineDetection(debug);

    debug.log("Extract connected components");
    ConnectedComponents connectedComponents = detector.detect(imageMat);
    debug.cc(connectedComponents);

    debug.log("Detect lines, sublines and subcolumns based on connected components");
    Lines lines = lineDetection.detectLines(connectedComponents);

    if (cleanSmallLines) {
      debug.log("Remove tiny lines (noise)");
      lines.removeSmallLines();
    }

    ImageSegmentationBuilder builder = new ImageSegmentationBuilder(binaryImageMat, lines, embedCrop, debug);
    imageSegmentation = builder.getImageSegmentation();
  }

  public void drawSegmentation(File outputImageFile) throws IOException {
    SegmentationDrawer drawer = new SegmentationDrawer(this, outputImageFile);
    drawer.drawSegmentation();
  }

  public Mat getImageMatClone() {
    return imageMat.clone();
  }

  public Mat getBinaryImageMatClone() {
    return binaryImageMat.clone();
  }
}
