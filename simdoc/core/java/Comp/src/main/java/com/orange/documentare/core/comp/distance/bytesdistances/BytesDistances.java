package com.orange.documentare.core.comp.distance.bytesdistances;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Denis Boisset & Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.comp.distance.computer.DistancesComputer;
import com.orange.documentare.core.comp.measure.ProgressListener;

public class BytesDistances {

  private final ProgressListener progressListener;

  public BytesDistances() {
    this(null);
  }
  public BytesDistances(ProgressListener progressListener) {
    this.progressListener = progressListener;
  }

  public DistancesArray computeDistancesBetweenCollections(BytesData[] bytesDataArray1, BytesData[] bytesDataArray2) {
    return compute(bytesDataArray1, bytesDataArray2);
  }

  public DistancesArray computeDistancesInCollection(BytesData[] bytesDataArray) {
    return compute(bytesDataArray, bytesDataArray);
  }

  private DistancesArray compute(BytesData[] bytesDataArray1, BytesData[] bytesDataArray2) {
    DistancesComputer computer = new DistancesComputer(bytesDataArray1, bytesDataArray2);
    computer.setProgressListener(progressListener);
    computer.compute();
    return computer.getDistancesArray();
  }
}
