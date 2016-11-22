package com.orange.documentare.app.ocr;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.ocr.cmdline.CommandLineOptions;
import com.orange.documentare.core.comp.multisets.MultiSetsBuilder;
import com.orange.documentare.core.comp.multisets.reco.OCR;
import com.orange.documentare.core.image.opencv.OpenCvImage;
import com.orange.documentare.core.image.opencv.OpencvLoader;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.ref.multisets.DigitalTypeClass;
import com.orange.documentare.core.model.ref.multisets.DigitalTypesClasses;
import com.orange.documentare.core.model.ref.multisets.MultiSet;
import com.orange.documentare.core.model.ref.multisets.MultiSets;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import com.orange.documentare.core.model.ref.text.ImageText;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.core.appender.SyslogAppender;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;

import java.io.File;
import java.io.IOException;

public class OcrApp {
  private static CommandLineOptions options;
  private static String OCR_DIR = "ocr";


  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
    try {
      options = new CommandLineOptions(args);
      doTheJob(options);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void doTheJob(CommandLineOptions options) throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);

    ImageSegmentation imageSegmentation = (ImageSegmentation) jsonGenericHandler.getObjectFromJsonFile(ImageSegmentation.class, options.getSegmentationInputFile());
    MultiSets multiSets = (MultiSets) jsonGenericHandler.getObjectFromJsonFile(MultiSets.class, options.getMultisetsInputFile());

    doOcr(multiSets, imageSegmentation.getDigitalTypes());
  }

  private static void doOcr(MultiSets multiSets, DigitalTypes digitalTypes) throws IOException {
    OpencvLoader.load();

    File dir = new File(OCR_DIR);
    FileUtils.deleteDirectory(dir);
    dir.mkdir();

    System.out.println("OCR...");
    OCR ocr = new OCR(multiSets);
    DigitalTypesClasses digitalTypeClasses = ocr.doRecoOn(digitalTypes);
    System.out.println("Write images...");
    for (int i = 0; i < digitalTypes.size(); i++) {
      DigitalType digitalType = digitalTypes.get(i);
      if (!digitalType.isSpace()) {
        writeOcrResult(digitalType, digitalTypeClasses.get(i));
      }
    }
    System.out.println("Done!");
  }

  private static void writeOcrResult(DigitalType digitalType, DigitalTypeClass digitalTypeClass) throws IOException {
    File ocrFile = buildOcrFileName(digitalType, digitalTypeClass);
    Mat mat = OpenCvImage.getMatFromSimDocBinaryDat(digitalType.getBytes());
    MatOfByte matOfByte = new MatOfByte();
    Highgui.imencode(".png", mat, matOfByte, new MatOfInt(Highgui.CV_IMWRITE_PNG_COMPRESSION, 0));
    FileUtils.writeByteArrayToFile(ocrFile, matOfByte.toArray());
  }

  private static File buildOcrFileName(DigitalType digitalType, DigitalTypeClass digitalTypeClass) {
    return new File(OCR_DIR + "/"
            + digitalTypeClass.getClazz()
            + "_"
            + digitalTypeClass.getNcd()
            + "_"
            + digitalType.getHumanReadableId()
            + ".png");
  }
}
