package com.orange.documentare.app.base64;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class Base64Converter {

  public static void convert(File src, File dest) throws IOException {
    byte[] bytes = FileUtils.readFileToByteArray(src);
    byte[] encodedBytes = Base64.getEncoder().encode(bytes);
    FileUtils.writeByteArrayToFile(dest, encodedBytes);
  }
}
