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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class RequestsExecutor {

  class ServiceStatus implements ServiceStatusListener {
    private final List<RemoteService> overloadedServices = new ArrayList<>();

    @Override
    public synchronized void serviceProvidedTaskResult(RemoteService remoteService) {
      overloadedServices.remove(remoteService);
    }
    @Override
    public synchronized void serviceCanNotHandleMoreTasks(RemoteService remoteService) {
      if (!overloadedServices.contains(remoteService)) {
        overloadedServices.add(remoteService);
      }
    }
    boolean isServiceAvailable(RemoteService remoteService) {
      return !overloadedServices.contains(remoteService);
    }
  }

  private static final String[] PROMPT = {"J", "O"};

  private final ServiceStatus serviceStatus = new ServiceStatus();
  private final RequestsProvider requestsProvider;
  private final ResponseCollector responseCollector;
  private final AvailableRemoteServices availableRemoteServices;

  private int promptIndex;

  public void exec() {
    do {
      availableRemoteServices.update();
      tryToDispatchNewRequests();
      sleepForAWhile();
    } while(!requestsProvider.empty() || !responseCollector.allResponsesCollected());
  }

  private void tryToDispatchNewRequests() {
    availableRemoteServices.services().stream()
      .filter(remoteService -> serviceStatus.isServiceAvailable(remoteService))
      .forEach(remoteService -> new Thread(run(remoteService)).start());
  }

  private Runnable run(RemoteService remoteService) {
    return () -> {
      // Optional is empty if no more request are pending (request provider is empty)
      ExecutorContext context = ExecutorContext.builder()
              .requestsProvider(requestsProvider)
              .responseCollector(responseCollector)
              .remoteService(remoteService)
              .serviceStatusListener(serviceStatus)
              .threadId(Thread.currentThread().getId())
              .build();
      requestsProvider.getPendingRequestExecutor()
        .ifPresent(executor -> executor.exec(context));
    };
  }

  private void sleepForAWhile() {
    try {
      System.out.print(PROMPT[promptIndex] + "\r");
      promptIndex = (promptIndex + 1) % 2;
      Thread.sleep(100);
    } catch (InterruptedException e) {
      log.warn("Waiting thread was interrupted: " + e.getMessage());
    }
  }
}
