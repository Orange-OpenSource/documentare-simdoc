package com.orange.documentare.core.comp.clustering.tasksservice;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.Getter;
import lombok.Synchronized;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * FIXME: No limitation right now, we should limit to #nbProcessors clustering tasks and
 * 1 distances+clustering task
 * FIXME: could be interesting to have a web service to cancel a task
 * RxJava or Reactor
 */
@Accessors(fluent = true)
@Slf4j
public class ClusteringTasksService {

  @Getter
  private static final ClusteringTasksService instance = new ClusteringTasksService();

  private final ClusteringTasks waitingTasks = new ClusteringTasks();
  private final ClusteringTasks runningTasks = new ClusteringTasks();
  private final ClusteringTasks doneTasks = new ClusteringTasks();
  private final ClusteringTasks errorTasks = new ClusteringTasks();

  @Synchronized
  public void addNewTask(ClusteringTask task) {
    if (isTaskRunning(task)) {
      throw new IllegalArgumentException("a task with the same id is already running");
    }
    waitingTasks.add(task);
    runNewTask();
  }

  @Synchronized
  public boolean isTaskRunning(ClusteringTask task) {
    return runningTasks.containsKey(task.id());
  }

  private void runNewTask() {
    if (!waitingTasks.isEmpty()) {
      ClusteringTask task = popWaitingTask();
      new Thread(() -> {
        runWaitingTaskInThread(task);
      }).start();
    }
  }

  private ClusteringTask popWaitingTask() {
    ClusteringTask task = waitingTasks.remove(waitingTasks.keySet().iterator().next());
    runningTasks.add(task);
    return task;
  }

  private void runWaitingTaskInThread(ClusteringTask task) {
    log.info(String.format("TASK [%s] START: %s", Thread.currentThread(), task));
    try {
      task.run();
      taskDone(task);
    } catch (IOException e) {
      taskError(task, e);
    }
  }

  @Synchronized
  private void taskDone(ClusteringTask task) {
    doneTasks.add(task);
    runningTasks.remove(task.id());
    log.info(String.format("TASK [%s] DONE: %s", Thread.currentThread(), task));
  }

  @Synchronized
  private void taskError(ClusteringTask task, IOException e) {
    String err = "TASK - Failed to build clustering for task: " + task + "/ exception: " + e.getMessage();
    task.taskError(err);
    errorTasks.add(task);
    runningTasks.remove(task.id());
    log.error(err, e);
  }

  @Synchronized
  public State taskState(String taskId) {
    if (waitingTasks.containsKey(taskId)) {
      return State.WAITING;
    }
    if (runningTasks.containsKey(taskId)) {
      return State.RUNNING;
    }
    if (doneTasks.containsKey(taskId)) {
      return State.DONE;
    }
    if (errorTasks.containsKey(taskId)) {
      return State.ERROR;
    }
    return State.UNKNOWN;
  }

  @Synchronized
  public String taskError(String taskId) {
    if (errorTasks.containsKey(taskId)) {
      return errorTasks.get(taskId).taskError();
    }
    return "TASK - not found: " + taskId;
  }

  @Synchronized
  public ClusteringTasksDescription tasksDescription() {
    return new ClusteringTasksDescription(waitingTasks, runningTasks, doneTasks, errorTasks);
  }

  /** for tests purpose */
  public void waitForAllTasksDone() throws InterruptedException {
    log.info("TASK - TEST, wait for all clustering tasks to be done on thread: " + Thread.currentThread());
    while(!waitingTasks.isEmpty() || !runningTasks.isEmpty()) {
      Thread.sleep(300);
    }
  }
}
