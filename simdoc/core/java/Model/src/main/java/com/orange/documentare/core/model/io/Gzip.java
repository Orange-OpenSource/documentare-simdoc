package com.orange.documentare.core.model.io;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class Gzip {

  public static String getStringFromGzipFile(File file) throws IOException {
    GZIPInputStream inputStream = new GZIPInputStream(new FileInputStream(file));
    String string = IOUtils.toString(inputStream);
    inputStream.close();
    return string;
  }
}
