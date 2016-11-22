package com.orange.documentare.core.image;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Binarization {

  private static final int ADAPTIVE_BLOCK_SIZE = 255;
  private static final double ADAPTIVE_MEAN_ADJUSTMENT = 0;

  public static Mat getFrom(Mat mat) {
    Mat greyscaleMat = isGreyscale(mat) ? mat : getGreyscaleImage(mat);
    Mat binaryMat = new Mat(greyscaleMat.size(), CvType.CV_8UC1);
    Imgproc.adaptiveThreshold(greyscaleMat, binaryMat, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, ADAPTIVE_BLOCK_SIZE, ADAPTIVE_MEAN_ADJUSTMENT);
    return binaryMat;
  }

  private static boolean isGreyscale(Mat mat) {
    return mat.channels() == 1;
  }

  private static Mat getGreyscaleImage(Mat image) {
    Mat greyImage = new Mat();
    Imgproc.cvtColor(image, greyImage, Imgproc.COLOR_RGB2GRAY);
    return greyImage;
  }
}
