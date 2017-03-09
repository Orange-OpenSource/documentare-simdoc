package com.orange.documentare.core.image.thumbnail;
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
import com.orange.documentare.core.system.nativeinterface.NativeInterface;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Thumbnail {
  private static final String[] SUPPORT_THUMBNAILS = {
    ".png", ".jpg", ".jpeg", ".tif", ".tiff", ".bmp", ".pdf"
  };

  private static int THUMBNAIL_SIDE = 200;

  private static final String CONVERT_CMD = "convert";

  // FIXME check options, update x/y option
  private static final String[] CONVERT_OPTIONS = {
    "-thumbnail", "x300"
  };

  public static boolean canCreateThumbnail(File file) throws IOException {
    File target;
    target = Files.isSymbolicLink(file.toPath()) ?
      Files.readSymbolicLink(file.toPath()).toFile() :
      file;

    String filename = target.getName().toLowerCase();
    return Arrays.asList(SUPPORT_THUMBNAILS).stream()
      .filter(extension -> filename.endsWith(extension))
      .count() > 0;
  }

  public static void createThumbnail(File image, File thumbnail) throws IOException {
    if (image == null || thumbnail == null) {
      throw new NullPointerException(String.format("Can not create thumbnail, provided image '%s' or thumbnail '%s' file is null", image, thumbnail));
    }

    List<String> options = new ArrayList<>(Arrays.asList(CONVERT_OPTIONS));
    options.add(0, image.getAbsolutePath() + "[0]");
    options.add(thumbnail.getAbsolutePath());
    NativeInterface.launch(
            CONVERT_CMD, options.toArray(new String[options.size()]), thumbnail.getAbsolutePath() + ".log");


    Mat mat = OpenCvImage.loadMat(image);
    if (mat.size().width == 0) {
      throw new IOException(String.format("Can not create thumbnail, provided image '%s' is invalid", image.getAbsolutePath()));
    }
    Mat thumbnailMat = new Mat();
    Imgproc.resize(mat, thumbnailMat, computeThumbnailSize(mat.size()));

    Highgui.imwrite(thumbnail.getAbsolutePath(), thumbnailMat);
  }

  private static Size computeThumbnailSize(Size imageSize) {
    if (imageSize.width < THUMBNAIL_SIDE && imageSize.height < THUMBNAIL_SIDE) {
      return imageSize;
    } else {
      double ratio = Math.min(THUMBNAIL_SIDE / imageSize.width, THUMBNAIL_SIDE / imageSize.height);
      return new Size(imageSize.width * ratio, imageSize.height * ratio);
    }
  }
}
