package com.orange.documentare.app.linedetection;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.image.opencv.OpenCvImage;
import com.orange.documentare.core.image.opencv.OpencvLoader;
import com.orange.documentare.core.image.segmentation.Segmenter;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.ref.doc.DocInfos;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import org.apache.commons.io.FileUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;

import java.io.File;
import java.io.IOException;

public class LineDetectionApp {

  private static final String OUT_JSON = "ld_segmentation.json.gz";
  private static final String OUT_IMG = "ld_segmentation.png";
  private static final String DEBUG_OUT_DIR = "ld_out";

  public static void main(String[] args) throws IOException {
    System.out.println("\n[LineDetection - Start]");
    OpencvLoader.load();
    try {
      doTheJob(args);
      System.out.println("\n[LineDetection - Done]");
    } catch(ArrayIndexOutOfBoundsException e) {
      showHelp();
    }
  }

  private static void doTheJob(String[] args) throws IOException {
    File imageFile = getFileFromPath(args[0]);
    Segmenter segmenter = new Segmenter(imageFile);
    segmenter.setEmbedCrop(true);
    segmenter.enableDebug();
    segmenter.doSegmentation();
    segmenter.drawSegmentation(new File(OUT_IMG));
    ImageSegmentation imageSegmentation = segmenter.getImageSegmentation();
    DocInfos docInfos = new DocInfos();
    docInfos.setPageId(imageFile.getName());
    imageSegmentation.setDocInfos(docInfos);
    saveSegmentation(imageSegmentation);
  }

  private static void saveSegmentation(ImageSegmentation segmentation) throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    jsonGenericHandler.writeObjectToJsonGzipFile(segmentation, new File(OUT_JSON));
    exportDigitalTypes(segmentation.getDigitalTypes());
  }

  private static void exportDigitalTypes(DigitalTypes digitalTypes) throws IOException {
    for (DigitalType digitalType : digitalTypes) {
      if (!digitalType.isSpace()) {
        exportDigitalType(digitalType);
      }
    }
  }

  private static void exportDigitalType(DigitalType digitalType) throws IOException {
    Mat mat = OpenCvImage.getMatFromSimDocBinaryDat(digitalType.getBytes());
    MatOfByte matOfByte = new MatOfByte();
    Highgui.imencode(".png", mat, matOfByte, new MatOfInt(Highgui.CV_IMWRITE_PNG_COMPRESSION, 0));
    String outImageName = String.format("%s/%d_%d.png", DEBUG_OUT_DIR, digitalType.y(), digitalType.x());
    String outRawImageName = String.format("%s/%d_%d.raw", DEBUG_OUT_DIR, digitalType.y(), digitalType.x());
    FileUtils.writeByteArrayToFile(new File(outImageName), matOfByte.toArray());
    FileUtils.writeByteArrayToFile(new File(outRawImageName), digitalType.getBytes());
  }

  private static File getFileFromPath(String path) {
    File file = new File(path);
    if (file.isFile()) {
      return file;
    } else {
      showHelp();
      throw new IllegalArgumentException(String.format("Failed to access file '%s'", path));
    }
  }

  private static void showHelp() {
    System.out.println("Please provide an image as first argument");
  }
}
