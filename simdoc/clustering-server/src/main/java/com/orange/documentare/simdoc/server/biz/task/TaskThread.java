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
    void error(Thread thread, String taskId);
  }

  private final ActiveThreadListener listener;
  private final String taskId;

  public TaskThread(Runnable runnable, ActiveThreadListener listener, String taskId) {
    super(runnable);
    this.listener = listener;
    this.taskId = taskId;
  }

  @Override
  public void run() {
    try {
      super.run();
      listener.finished(this);
    } catch (Exception | Error e) {
      log.error("Thread crashed", e);
      listener.error(this, taskId);
    }
  }
}
