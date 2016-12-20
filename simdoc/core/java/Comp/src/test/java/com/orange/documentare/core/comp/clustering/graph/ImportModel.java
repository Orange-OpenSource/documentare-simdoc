package com.orange.documentare.core.comp.clustering.graph;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
public class ImportModel {
  private InputItem[] items;

  public void loadItemsBytes() throws IOException {
    for (InputItem inputItem : items) {
      loadItemBytes(inputItem);
    }
  }

  private void loadItemBytes(InputItem inputItem) throws IOException {
    StringBuilder stringBuilder = new StringBuilder(inputItem.getFileNameReversed());
    String filename = stringBuilder.reverse().toString();
    byte[] bytes = FileUtils.readFileToByteArray(new File(getClass().getResource(filename).getFile()));
    inputItem.setBytes(bytes);
  }
}
