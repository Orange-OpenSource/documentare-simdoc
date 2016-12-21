package com.orange.documentare.core.comp.measure;
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

public interface ProgressListener {
  /**
   * @param step
   * @param progress */
  void onProgressUpdate(TreatmentStep step, Progress progress);
}
