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

public final class MemoryState {
  private static final float GB = 1024 * 1024 * 1024;

  public final float totalMemory;
  public final float maxMemory;
  public final int usedMemory;

  public MemoryState() {
    Runtime runtime = Runtime.getRuntime();

    this.totalMemory = (float)runtime.totalMemory() / GB;
    this.maxMemory = runtime.maxMemory() / GB;
    this.usedMemory = (int) (totalMemory / maxMemory * 100);
  }

  public String displayString() {
    return String.format("Mem %d%% %.1f/%.1fGB", usedMemory, totalMemory, maxMemory);
  }
}
