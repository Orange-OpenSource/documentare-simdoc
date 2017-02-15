package com.orange.documentare.core.image.thumbnail;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.system.measure.Progress;

class ThumbnailProgress {
  private final int numberOfThumbnails;
  private final long t0;

  private int thumbnailCreatedCount;

  ThumbnailProgress(int numberOfThumbnails) {
    this.numberOfThumbnails = numberOfThumbnails;
    this.t0 = System.currentTimeMillis();
  }

  synchronized
  void newThumbnailCreated() {
    thumbnailCreatedCount++;
  }

  Progress progress() {
    int percent = (int)((float)thumbnailCreatedCount / numberOfThumbnails * 100);
    int dt = (int) (System.currentTimeMillis() - t0) / 1000;
    return new Progress(percent, dt);
  }
}
