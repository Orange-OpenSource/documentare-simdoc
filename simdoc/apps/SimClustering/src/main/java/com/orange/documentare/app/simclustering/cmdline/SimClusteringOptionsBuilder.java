package com.orange.documentare.app.simclustering.cmdline;
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

public class SimClusteringOptionsBuilder {
  private File regularFile;
  private File simdocFile;
  private float qcutSdFactor = -1;
  private float acutSdFactor = -1;
  private float scutSdFactor = -1;
  private int ccutPercentile = -1;
  private boolean wcut;

  public void qcut(String optionValue) {
    qcutSdFactor = Float.parseFloat(optionValue);
  }

  public void acut(String optionValue) {
    acutSdFactor = Float.parseFloat(optionValue);
  }

  public void scut(String optionValue) {
    scutSdFactor = Float.parseFloat(optionValue);
  }

  public void ccut(String optionValue) {
    ccutPercentile = Integer.parseInt(optionValue);
  }

  public void wcut() {
    wcut = true;
  }

  public void regularFile(String optionValue) {
    regularFile = new File(optionValue);
  }

  public void simdocFile(String optionValue) {
    simdocFile = new File(optionValue);
  }

  public SimClusteringOptions build() {
    checkOptions();
    return new SimClusteringOptions(regularFile, simdocFile, qcutSdFactor, acutSdFactor, scutSdFactor, ccutPercentile, wcut);
  }

  private void checkOptions() {

  }
}
