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

import java.io.File;

public class OpenCvImage {

  public static final byte[] SIMDOC_MAGIC_NUMBER = "JoTOphe".getBytes();
  public static final byte SIMDOC_LINE_TERMINATION = '\n';

  /**
   * @param imageFile
   * @return image OpenCV Mat
   */
  public static Mat getMat(File imageFile) {
    return Highgui.imread(imageFile.getAbsolutePath());
  }


  /**
   * @param image mat
   * @return byte[] image bytes array
   */
  public static byte[] getRawBytesOf(Mat image) {
    return getBytesOf(image, false);
  }

  /**
   * Get image bytes following the SimDoc format:
   *  - we had a magic key header
   *  - we had an image row termination character
   * @param image mat
   * @return byte[] image bytes array
   */
  public static byte[] getSimDocBytesOf(Mat image) {
    return getBytesOf(image, true);
  }

  private static byte[] getBytesOf(Mat image, boolean simDocFormat) {
    byte[] byteArray = getImageBytesCount(image, simDocFormat);
    fillByteArray(byteArray, image, simDocFormat);
    return byteArray;
  }


  /**
   * @param bytes image bytes
   * @param rows
   * @param columns
   * @return Mat
   */
  public static Mat getMatFromBinaryDat(byte[] bytes, int rows, int columns) {
    return getMatFromBinaryDat(bytes, rows, columns, false);
  }

  /**
   * @param bytes image bytes in SimDoc format
   * @return Mat
   */
  public static Mat getMatFromSimDocBinaryDat(byte[] bytes) {
    int simDocOffset = SIMDOC_MAGIC_NUMBER.length;
    int columns = getColumnsCountOf(bytes);
    int rows = (bytes.length - simDocOffset) / (columns + 1);
    return getMatFromBinaryDat(bytes, rows, columns, true);
  }

  private static Mat getMatFromBinaryDat(byte[] bytes, int rows, int columns, boolean simDocFormat) {
    int simDocOffset = simDocFormat ? SIMDOC_MAGIC_NUMBER.length : 0;
    int simDocLineExtra = simDocFormat ? 1 : 0;
    Mat mat = new Mat(rows, columns, CvType.CV_8UC1);
    byte[] dat = new byte[1];
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < columns; x++) {
        dat[0] = bytes[simDocOffset + y * (columns + simDocLineExtra) + x];
        mat.put(y, x, dat);
      }
    }
    return mat;
  }

  private static int getColumnsCountOf(byte[] bytes) {
    int count = 0;
    for (int i = SIMDOC_MAGIC_NUMBER.length; i < bytes.length; i++) {
      if (bytes[i] == SIMDOC_LINE_TERMINATION) {
        return count;
      } else {
        count++;
      }
    }
    return count;
  }

  private static byte[] getImageBytesCount(Mat image, boolean simDocFormat) {
    int rawBytesCount = image.channels() * image.rows() * image.cols();
    int simDocExtra = simDocFormat ? SIMDOC_MAGIC_NUMBER.length + image.rows() : 0;
    return new byte[rawBytesCount + simDocExtra];
  }

  private static void fillByteArray(byte[] byteArray, Mat image, boolean simDocFormat) {
    int colsNb = image.cols();
    int bytesPerPixel = image.channels();
    int bytesPerRow = colsNb * bytesPerPixel + (simDocFormat ? 1 : 0);
    byte[] pixel = new byte[bytesPerPixel];
    int magicNumberOffset = 0;
    if (simDocFormat) {
      magicNumberOffset = SIMDOC_MAGIC_NUMBER.length;
      addSimDocMagicNumber(byteArray);
    }
    for (int y = 0; y < image.rows(); y++) {
      for (int x = 0; x < colsNb; x++) {
        image.get(y, x, pixel);
        for (int z = 0; z < bytesPerPixel; z++) {
          byteArray[magicNumberOffset + y * bytesPerRow + x * bytesPerPixel + z] = pixel[z];
        }
      }
      if (simDocFormat) {
        byteArray[magicNumberOffset + y * bytesPerRow + colsNb * bytesPerPixel] = SIMDOC_LINE_TERMINATION;
      }
    }
  }

  private static void addSimDocMagicNumber(byte[] byteArray) {
    System.arraycopy(SIMDOC_MAGIC_NUMBER, 0, byteArray, 0, SIMDOC_MAGIC_NUMBER.length);
  }
}
