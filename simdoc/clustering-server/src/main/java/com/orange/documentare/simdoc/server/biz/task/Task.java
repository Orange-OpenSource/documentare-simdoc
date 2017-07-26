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

import java.util.Optional;
import java.util.UUID;

public class Task {
  public final String id;
  public final Optional<Object> result;
  public final boolean error;

  public Task withResult(Object result) {
    return new Task(id, Optional.of(result), false);
  }

  public Task withErrorResult(Object result) {
    return new Task(id, Optional.of(result), true);
  }

  Task() {
    this(UUID.randomUUID().toString(), Optional.empty(), false);
  }

  private Task(String id, Optional<Object> result, boolean error) {
    this.id = id;
    this.result = result;
    this.error = error;
  }
}
