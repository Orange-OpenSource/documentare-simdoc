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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpencvLoader {

  private static final String OPENCV_MAC_LIB = "/opt/opencv-mac-lib/libopencv_java249.dylib";

  public static void load() {
    showMessage("Try to load OpenCV native library (" + org.opencv.core.Core.NATIVE_LIBRARY_NAME + ")");
    try {
      System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    } catch (UnsatisfiedLinkError e) {
      showMessage("Failed to load opencv library from system installation, let's try with nu package provided libraries");
      nu.pattern.OpenCV.loadShared();
    }
  }

  private static void showMessage(String message) {
    log.info(message);
  }
}
