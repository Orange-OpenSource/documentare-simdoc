package com.orange.documentare.core.prepdata;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Denis Boisset & Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */


import com.orange.documentare.core.image.opencv.OpenCvImage;
import com.orange.documentare.core.system.inputfilesconverter.FileConverter;
import com.orange.documentare.core.system.inputfilesconverter.SymbolicLinkConverter;
import org.apache.commons.io.FileUtils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class RawFilesConverter implements FileConverter {

  private static final String[] IMAGES_EXTENSION = {
    ".png", ".jpg", ".jpeg", ".tif", ".tiff", ".bmp"
  };

  private final SymbolicLinkConverter symbolicLinkConverter = new SymbolicLinkConverter();

  @Override
  public void convert(File source, File destination) {
    if (isImage(source)) {
      convertToRaw(source, destination);
    } else {
      symbolicLinkConverter.convert(source, destination);
    }
  }

  private void convertToRaw(File source, File destination) {
    Mat mat = OpenCvImage.loadMat(source);
    byte[] bytes = OpenCvImage.matToRaw(mat);
    try {
      FileUtils.writeByteArrayToFile(destination, bytes);
    } catch (IOException e) {
      throw new IllegalStateException(String.format("Failed to write raw file to '%s': %s", destination.getAbsolutePath(), e.getMessage()));
    }
  }

  private boolean isImage(File source) {
    String lowerCaseFilename = source.getName().toLowerCase();
    return Arrays.stream(IMAGES_EXTENSION)
      .filter(ext -> lowerCaseFilename.endsWith(ext))
      .count() > 0;
  }
}
