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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemoryWatcher extends Thread {

  private static final MemoryWatcher sMemoryWatcher = new MemoryWatcher();

  private static final String[] TICK = {
    "J", "O", "E", "L"
  };

  private boolean stop;

  public static void watch() {
    sMemoryWatcher.start();
  }

  public static void stopWatching() {
    sMemoryWatcher.stop = true;
  }

  private MemoryWatcher() {
    super();
  }

  @Override
  public void run() {
    int count = 0;
    do {
      MemoryState memoryState = new MemoryState();
      System.out.print("\r" + TICK[count] + " - " + memoryState.displayString());
      count = (count + 1) % TICK.length;

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        log.error("[MemoryWatcher] Error while sleeping", e);
      }
    } while(!stop);
  }
}
