package com.orange.documentare.core.system.measure;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

public final class Progress {

  public final int percent;
  public final int elapsedInSeconds;
  public final int timeLeftInSeconds;
  public final MemoryState memoryState = new MemoryState();

  public Progress(int percent, int elapsedInSeconds) {
    this.percent = percent;
    this.elapsedInSeconds = elapsedInSeconds;
    this.timeLeftInSeconds = computeTimeLeftInSeconds();
  }

  private int computeTimeLeftInSeconds() {
    return (int) (elapsedInSeconds * ((float) 100 / percent - 1));
  }

  public String displayString(String message) {
    return String.format("[%s] %d%%, ela %ds, left %ds, %s                    ",
            message, percent, elapsedInSeconds, timeLeftInSeconds, memoryState.displayString());
  }
}
