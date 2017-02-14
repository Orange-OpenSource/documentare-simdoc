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
import java.util.HashMap;

public class FilesMap extends HashMap<Integer, String> {

  public String simpleFilenameAt(int index) {
    String absFilename = get(index);
    int lastIndexOfDirSep = absFilename.lastIndexOf(File.separator);
    return lastIndexOfDirSep < 0 ?
      absFilename :
      absFilename.substring(lastIndexOfDirSep + 1);
  }
}
