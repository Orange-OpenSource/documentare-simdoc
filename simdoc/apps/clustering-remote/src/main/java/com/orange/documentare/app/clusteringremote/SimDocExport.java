package com.orange.documentare.app.clusteringremote;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor(suppressConstructorProperties = true)
class SimDocExport {
  private final ImageSegmentation imageSegmentation;

  public void exportTo(File simDocExportFile) throws IOException {
    clearGlyphs(imageSegmentation.getDigitalTypes());
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    jsonGenericHandler.writeObjectToJsonGzipFile(imageSegmentation, simDocExportFile);
  }

  private void clearGlyphs(DigitalTypes digitalTypes) {
    for (DigitalType digitalType : digitalTypes) {
      digitalType.setBytes(null);
      digitalType.setNearestItems(null);
    }
  }
}
