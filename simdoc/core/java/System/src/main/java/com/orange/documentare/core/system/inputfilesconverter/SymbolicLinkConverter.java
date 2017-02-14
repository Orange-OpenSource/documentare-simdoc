package com.orange.documentare.core.system.inputfilesconverter;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SymbolicLinkConverter implements FileConverter {

  @Override
  public void convert(File source, File destination) {
    Path src = source.toPath().toAbsolutePath();
    Path dest = destination.toPath().toAbsolutePath();
    try {
      Files.createSymbolicLink(dest, src);
    } catch (IOException e) {
      throw new FileConverterException(String.format("Failed to create symbolic link '%s' to '%s'", dest, src));
    }
  }
}
