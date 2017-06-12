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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class Tasks implements TaskThread.ActiveThreadListener {

  private final Map<String, Task> tasks = new HashMap<>();
  private final int maxTasks = Runtime.getRuntime().availableProcessors();
  private final List<Thread> activeThreads = new ArrayList<>();



  public synchronized String newTask() {
    Task task = new Task();
    tasks.put(task.id, task);
    return task.id;
  }

  public synchronized boolean exists(String id) {
    return tasks.get(id) != null;
  }

  public synchronized boolean isDone(String id) {
    Task task = tasks.get(id);
    if (task == null) {
      log.warn("Invalid task id: " + id);
      return false;
    }
    return task.result.isPresent();
  }

  public synchronized void addResult(String id, Object result) {
    tasks.put(id, tasks.get(id).withResult(result));
  }

  public void addErrorResult(String id, Object result) {
    tasks.put(id, tasks.get(id).withErrorResult(result));
  }

  public synchronized Task pop(String id) {
    Task task = tasks.get(id);
    tasks.remove(id);
    return task;
  }

  public synchronized boolean present(String id) {
    return tasks.containsKey(id);
  }

  public synchronized void run(Runnable runnable) {
    Thread thread = new TaskThread(runnable, this);
    activeThreads.add(thread);
    thread.start();
  }

  @Override
  public synchronized void finished(Thread thread) {
    activeThreads.remove(thread);
  }

  public boolean canAcceptNewTask() {
    return activeThreads.size() < maxTasks;
  }

  public synchronized void killAll() {
    tasks.clear();
    activeThreads.forEach(thread -> {
      try {
        thread.stop();
      } catch (ThreadDeath e) {
        // catch silently
      }
    });
    activeThreads.clear();
  }
}
