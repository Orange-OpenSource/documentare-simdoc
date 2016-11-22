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

import java.util.ArrayList;

public class ClusteringTasksDescription extends ArrayList<ClusteringTaskDescription> {

  public ClusteringTasksDescription(ClusteringTasks waitingTasks, ClusteringTasks runningTasks, ClusteringTasks doneTasks, ClusteringTasks errorTasks) {
    init(waitingTasks, runningTasks, doneTasks, errorTasks);
  }

  private void init(ClusteringTasks waitingTasks, ClusteringTasks runningTasks, ClusteringTasks doneTasks, ClusteringTasks errorTasks) {
    for (ClusteringTask task : runningTasks.values()) {
      add(new ClusteringTaskDescription("running", task.id(), task.progressString()));
    }
    for (ClusteringTask task : waitingTasks.values()) {
      add(new ClusteringTaskDescription("waiting", task.id(), task.progressString()));
    }
    for (ClusteringTask task : errorTasks.values()) {
      add(new ClusteringTaskDescription("error", task.id(), task.progressString()));
    }
    for (ClusteringTask task : doneTasks.values()) {
      add(new ClusteringTaskDescription("done", task.id(), task.progressString()));
    }
  }
}
