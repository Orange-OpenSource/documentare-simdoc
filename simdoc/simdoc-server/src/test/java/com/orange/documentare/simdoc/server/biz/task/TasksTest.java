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

import com.orange.documentare.simdoc.server.biz.distances.DistancesRequestResult;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TasksTest {

  @Test
  public void task_id_is_unique() {
    // Given
    Tasks tasks = new Tasks();

    // When
    String id1 = tasks.newTask();
    String id2 = tasks.newTask();

    // Then
    Assertions.assertThat(id1).isNotEqualTo(id2);
  }

  @Test
  public void add_task_then_flag_it_as_finished_then_remove_it_when_retrieved() {
    // Given
    Tasks tasks = new Tasks();
    Object result = new Object();

    // When / Then
    String id = tasks.newTask();
    System.out.println("task id = " + id);

    Assertions.assertThat(tasks.isDone(id)).isFalse();
    tasks.addResult(id, result);
    Assertions.assertThat(tasks.isDone(id)).isTrue();

    Task task = tasks.pop(id);
    Assertions.assertThat(tasks.present(id)).isFalse();
    Assertions.assertThat(task.result.get()).isEqualTo(result);
  }

  @Test
  public void add_error_result() {
    // Given
    Tasks tasks = new Tasks();
    DistancesRequestResult result = DistancesRequestResult.error("err");
    String id = tasks.newTask();

    // When
    tasks.addErrorResult(id, result);
    Task task = tasks.pop(id);

    // Then
    Assertions.assertThat(task.error).isTrue();
  }
}
