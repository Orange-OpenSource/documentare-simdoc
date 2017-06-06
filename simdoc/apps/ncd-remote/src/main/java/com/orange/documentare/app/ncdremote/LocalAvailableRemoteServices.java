package com.orange.documentare.app.ncdremote;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.google.common.collect.ImmutableList;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class LocalAvailableRemoteServices implements AvailableRemoteServices {
  @Setter
  private RequestsExecutor requestExecutor;

  private final List<RemoteService> availableServices = new ArrayList<>();

  @Override
  public void update() {
    if (requestExecutor.idle()) {
      addOneLocalServicePerCpuThread();
    } else {
      availableServices.clear();
    }
  }

  private void addOneLocalServicePerCpuThread() {
    IntStream.range(0, Runtime.getRuntime().availableProcessors())
            .forEach(i -> availableServices.add(new RemoteService("http://localhost:8080")));
    //IntStream.range(0, Runtime.getRuntime().availableProcessors())
    //        .forEach(i -> availableServices.add(new RemoteService("http://g-z620-4lfq:8080")));
    //IntStream.range(0, /* FIXME Runtime.getRuntime().availableProcessors() */ 8)
    //        .forEach(i -> availableServices.add(new RemoteService("http://g-z440-cm:8080")));
  }

  @Override
  public int threadsCount() {
    return availableServices.size();
  }

  @Override
  public List<RemoteService> services() {
    return ImmutableList.copyOf(availableServices);
  }
}
