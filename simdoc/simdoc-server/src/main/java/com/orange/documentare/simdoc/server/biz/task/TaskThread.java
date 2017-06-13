package com.orange.documentare.simdoc.server.biz.task;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TaskThread extends Thread {

  interface ActiveThreadListener {
    void finished(Thread thread);
  }

  private ActiveThreadListener listener;

  public TaskThread(Runnable runnable, ActiveThreadListener listener) {
    super(runnable);
    this.listener = listener;
  }

  @Override
  public void run() {

    try {
      super.run();
    } catch (Exception e) {
      e.printStackTrace();
      log.error("Thread crashed", e);
    }

    listener.finished(this);
  }
}
