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

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Tasks {

  private final Map<String, Task> tasks = new HashMap<>();

  public synchronized String newTask() {
    Task task = new Task();
    tasks.put(task.id, task);
    return task.id;
  }

  public synchronized boolean isDone(String id) {
    return tasks.get(id).result.isPresent();
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
}
