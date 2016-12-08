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

import java.util.HashMap;

class ClusteringTasks extends HashMap<String, ClusteringTask> {
  void add(ClusteringTask task) {
    put(task.id(), task);
  }
}
