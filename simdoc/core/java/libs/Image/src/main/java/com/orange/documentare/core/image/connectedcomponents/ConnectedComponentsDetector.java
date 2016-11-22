package com.orange.documentare.core.image.connectedcomponents;
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
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConnectedComponentsDetector {

  interface ConnectedComponentsFilter {
    ConnectedComponents filter(ConnectedComponents connectedComponents);
  }

  /**
   * Retrieve image connected components
   * @param imageMat
   * @return connected components
   */
  public ConnectedComponents detect(Mat imageMat) {
    return detect(imageMat, new SimpleConnectedComponentsFilter(imageMat.width()));
  }

  /**
   * Retrieve image connected components
   * @param imageFile
   * @return connected components
   */
  public ConnectedComponents detect(File imageFile) {
    Mat mat = Highgui.imread(imageFile.getAbsolutePath());
    return detect(mat);
  }

  /**
   * Retrieve image connected components in image
   * @param imageMat
   * @param filter to apply to remove some connected components (based on size, etc)
   * @return connected components
   */
  private ConnectedComponents detect(Mat imageMat, ConnectedComponentsFilter filter) {
    Mat binaryMat = Binarization.getFrom(imageMat);
    List<MatOfPoint> contours = new ArrayList<>();
    Mat hierarchy = new Mat();
    Imgproc.findContours(binaryMat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
    return buildConnectedComponents(contours, filter);
  }


  private ConnectedComponents buildConnectedComponents(List<MatOfPoint> contours, ConnectedComponentsFilter filter) {
    ConnectedComponents connectedComponents = buildRawConnectedComponents(contours);
    if (filter != null) {
      ConnectedComponents toRemove = filter.filter(connectedComponents);
      connectedComponents.removeAll(toRemove);
    }
    return connectedComponents;
  }

  private ConnectedComponents buildRawConnectedComponents(List<MatOfPoint> contours) {
    ConnectedComponents connectedComponents = new ConnectedComponents();
    for (MatOfPoint point : contours) {
      ConnectedComponent connectedComponent = buildConnectedComponentFromContour(point);
      if (connectedComponent.isNotEmpty()) {
        connectedComponents.add(connectedComponent);
      }
    }
    return connectedComponents;
  }

  /**
   * Adjust bounding segmentationcollection: external char contour is outside char "black" pixels,
   * so we need to adjust the bounding segmentationcollection so that external "white" pixels are not
   * part of the bounding segmentationcollection.
   * @param point
   * @return connectedComponent
   */
  private ConnectedComponent buildConnectedComponentFromContour(MatOfPoint point) {
    Rect rect = Imgproc.boundingRect(point);
    rect.x += 1;
    rect.y += 1;
    rect.width -= 3;
    rect.height -= 3;
    return new ConnectedComponent(rect.x, rect.y, rect.width, rect.height);
  }
}
