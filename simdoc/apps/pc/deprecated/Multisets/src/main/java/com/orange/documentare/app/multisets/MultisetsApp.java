package com.orange.documentare.app.multisets;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.multisets.cmdline.CommandLineOptions;
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
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MultisetsApp {
  private static CommandLineOptions options;
  private static String MULTISETS_DIR = "multisets";
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
    ImageText imageText = (ImageText) jsonGenericHandler.getObjectFromJsonFile(ImageText.class, options.getTextInputFile());

    MultiSetsBuilder multiSetsBuilder = new MultiSetsBuilder(options.isPropagatedCharIncluded(), options.getClassMinSize());
    System.out.println("Class min size set to: " + options.getClassMinSize());
    System.out.println("Class max size set to: " + options.getClassMaxSize());
    System.out.println("Include propagated chars: " + options.isPropagatedCharIncluded());
    MultiSets multiSets = multiSetsBuilder.build(imageText);
    if (options.isClassMaxSizeAvailable()) {
      multiSetsBuilder.limitClassSize(multiSets, imageSegmentation, options.getClassMaxSize());
    }
    multiSetsBuilder.addBytes(multiSets, imageSegmentation);

    showMultisetsInfo(multiSets, imageSegmentation);
    if (multiSets.size() > 0) {
      jsonGenericHandler.writeObjectToJsonFile(multiSets, new File("multisets.json"));
      writeMultisets(multiSets);
      doOcr(multiSets, imageSegmentation.getDigitalTypes());
    }
  }

  private static void showMultisetsInfo(MultiSets multiSets, ImageSegmentation imageSegmentation) {
    System.out.println("===============================");
    if (multiSets.size() == 0) {
      System.out.println("No multiset built");
    }
    multiSets.stream().forEach(multiSet -> showMultisetsInfo(multiSet, imageSegmentation.getDigitalTypes()));
    System.out.println("===============================");
  }

  private static void showMultisetsInfo(MultiSet multiSet, DigitalTypes digitalTypes) {
    System.out.println(String.format("Multiset created for string '%s' with %d elements (centers = %s)",
            multiSet.getClazz(), multiSet.getDigitalTypeIndices().size(),
            centersOf(multiSet, digitalTypes)));

  }

  private static String centersOf(MultiSet multiSet, DigitalTypes digitalTypes) {
    List<Integer> centers = multiSet.getDigitalTypeIndices().stream()
            .filter(index -> digitalTypes.get(index).isClusterCenter())
            .collect(Collectors.toList());
    return centers.size() + " / " +
            centers.stream()
            .map(index -> digitalTypes.get(index).getHumanReadableId())
            .collect(Collectors.joining(" "));
  }

  private static void writeMultisets(MultiSets multiSets) throws IOException {
    File dir = new File(MULTISETS_DIR);
    FileUtils.deleteDirectory(dir);
    dir.mkdir();
    multiSets.stream().forEach(multiSet -> writeMultiset(multiSet));
  }

  private static void writeMultiset(MultiSet multiSet) {
    File multisetFile = new File(MULTISETS_DIR + "/" + multiSet.getClazz() + ".bin");
    try {
      FileUtils.writeByteArrayToFile(multisetFile, multiSet.getDat());
    } catch (IOException e) {
      System.out.println("Failed to write file: " + multisetFile.getAbsolutePath() + " / exception: " + e.getMessage());
    }
  }

  private static void doOcr(MultiSets multiSets, DigitalTypes digitalTypes) throws IOException {
    OpencvLoader.load();

    File dir = new File(OCR_DIR);
    FileUtils.deleteDirectory(dir);
    dir.mkdir();

    OCR ocr = new OCR(multiSets);
    DigitalTypesClasses digitalTypeClasses = ocr.doRecoOn(digitalTypes);
    for (int i = 0; i < digitalTypes.size(); i++) {
      DigitalType digitalType = digitalTypes.get(i);
      if (!digitalType.isSpace()) {
        writeOcrResult(digitalType, digitalTypeClasses.get(i));
      }
    }
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
            + (digitalType.isClusterCenter() ? "BC" + digitalType.getClusterId() + "_" : "")
            + digitalTypeClass.getNcd()
            + "_"
            + digitalType.getHumanReadableId()
            + ".png");
  }
}
