package com.orange.documentare.app.ncdremote;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestAnimalsElements {

  public BytesData[] elements() {
    File directory = new File(getClass().getResource("/animals-dna/").getFile());
    return BytesData.buildFromDirectoryWithoutBytes(directory, file -> file.getName());
  }
}
