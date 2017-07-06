package com.orange.documentare.core.image.opencv;
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
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class OpenCvImage {

  public static final byte SIMDOC_LINE_TERMINATION = '\n';

  /**
   * @param imageFile
   * @return image OpenCV Mat
   */
  public static Mat loadMat(File imageFile) {
    return Highgui.imread(imageFile.getAbsolutePath());
  }


  public static Mat resize(Mat mat, int bytesSizeTarget) {
    long matBytesCount = matBytesCount(mat);
    float ratio = (float)matBytesCount / bytesSizeTarget;
    double sqrtRatio = Math.sqrt(ratio);
    int newWidth = (int) (mat.size().width / sqrtRatio);
    int newHeigth = (int) (mat.size().height / sqrtRatio);
    Mat newMat = new Mat(newHeigth, newWidth, mat.type());
    Imgproc.resize(mat, newMat, newMat.size(), sqrtRatio, sqrtRatio, Imgproc.INTER_LANCZOS4);
    return newMat;
  }

  private static long matBytesCount(Mat mat) {
    long pixelBytesCount = mat.elemSize();
    return pixelBytesCount * mat.total();
  }

  /**
   * @param image mat
   * @return byte[] image bytes array
   */
  public static byte[] matToBytes(Mat image) {
    return getBytesOf(image, false);
  }

  /**
   * Get image bytes following the SimDoc format:
   *  - we had a magic key header
   *  - we had an image row termination character
   * @param image mat
   * @return byte[] image bytes array
   */
  public static byte[] matToRaw(Mat image) {
    return getBytesOf(image, true);
  }

  private static byte[] getBytesOf(Mat image, boolean raw) {
    byte[] byteArray = computeImageBytesCount(image, raw);
    fillByteArray(byteArray, image, raw);
    return byteArray;
  }


  /**
   * @param bytes image bytes
   * @param rows
   * @param columns
   * @return Mat
   */
  public static Mat bytesToMat(byte[] bytes, int rows, int columns) {
    return bytesToMat(bytes, rows, columns, false);
  }

  /**
   * @param bytes image bytes in SimDoc format
   * @return Mat
   */
  public static Mat rawToMat(byte[] bytes) {
    int columns = computeColumnsCountOf(bytes);
    int rows = bytes.length / (columns + 1);
    return bytesToMat(bytes, rows, columns, true);
  }

  private static Mat bytesToMat(byte[] bytes, int rows, int columns, boolean raw) {
    int simDocLineExtra = raw ? 1 : 0;
    Mat mat = new Mat(rows, columns, CvType.CV_8UC1);
    byte[] dat = new byte[1];
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < columns; x++) {
        dat[0] = bytes[y * (columns + simDocLineExtra) + x];
        mat.put(y, x, dat);
      }
    }
    return mat;
  }

  private static int computeColumnsCountOf(byte[] bytes) {
    int count = 0;
    for (int i = 0; i < bytes.length; i++) {
      if (bytes[i] == SIMDOC_LINE_TERMINATION) {
        return count;
      } else {
        count++;
      }
    }
    return count;
  }

  private static byte[] computeImageBytesCount(Mat image, boolean raw) {
    int rawBytesCount = image.channels() * image.rows() * image.cols();
    int simDocExtra = raw ? image.rows() : 0;
    return new byte[rawBytesCount + simDocExtra];
  }

  private static void fillByteArray(byte[] byteArray, Mat image, boolean raw) {
    int colsNb = image.cols();
    int bytesPerPixel = image.channels();
    int bytesPerRow = colsNb * bytesPerPixel + (raw ? 1 : 0);
    byte[] pixel = new byte[bytesPerPixel];
    int magicNumberOffset = 0;
    for (int y = 0; y < image.rows(); y++) {
      for (int x = 0; x < colsNb; x++) {
        image.get(y, x, pixel);
        for (int z = 0; z < bytesPerPixel; z++) {
          byteArray[magicNumberOffset + y * bytesPerRow + x * bytesPerPixel + z] = pixel[z];
        }
      }
      if (raw) {
        byteArray[magicNumberOffset + y * bytesPerRow + colsNb * bytesPerPixel] = SIMDOC_LINE_TERMINATION;
      }
    }
  }
}
