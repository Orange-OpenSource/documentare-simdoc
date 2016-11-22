package com.orange.documentare.app.ncd;
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
import java.util.Comparator;

public class FilenameOrder implements Comparator<File> {
  @Override
  public int compare(File file1, File file2) {
    return file1.getName().compareTo(file2.getName());
  }
}
