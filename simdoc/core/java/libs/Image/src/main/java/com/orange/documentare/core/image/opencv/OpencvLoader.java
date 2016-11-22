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

import lombok.extern.log4j.Log4j2;

@Log4j2
public class OpencvLoader {

  private static final String OPENCV_MAC_LIB = "/opt/opencv-mac-lib/libopencv_java249.dylib";

  public static void load() {
    showMessage("Try to load OpenCV native library");
    try {
      System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    } catch (UnsatisfiedLinkError e) {
      showRegularLoadError();
      loadMacLib();
    }
  }

  private static void loadMacLib() {
    try {
      System.load(OPENCV_MAC_LIB);
      showMessage("Library loaded, great :)");
    } catch(UnsatisfiedLinkError e) {
      showMacLoadError();
    }
  }

  private static void showRegularLoadError() {
    showMessage("Failed to load opencv library through regular api, let's try with absolute path name for mac.");
  }

  private static void showMacLoadError() {
    showMessage(String.format("Failed to load library with absolute path name.\nPlease check you have the library installed here: %s", OPENCV_MAC_LIB));
  }

  private static void showMessage(String message) {
    log.info(message);
  }
}
