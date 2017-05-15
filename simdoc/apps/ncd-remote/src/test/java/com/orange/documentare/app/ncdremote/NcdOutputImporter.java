package com.orange.documentare.app.ncdremote;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public class NcdOutputImporter {
  @RequiredArgsConstructor
  public static class Item {
    public final String relativeFilename;

    public Item() {
      relativeFilename = null;
    }
  }

  public final Item[] items1;
  public final Item[] items2;
  public final DistancesArray distancesArray;

  public NcdOutputImporter() {
    items1 = null;
    items2 = null;
    distancesArray = null;
  }
}
