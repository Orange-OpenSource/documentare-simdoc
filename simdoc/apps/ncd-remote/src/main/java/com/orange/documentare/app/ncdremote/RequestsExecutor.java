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
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
public class RequestsExecutor {

  class RemoteServicesThreads {
    private final List<RemoteService> availableRemoteServices = new ArrayList<>();
    private int allocatedThreadsCount;

    public void addAvailableRemoteServices(List<RemoteService> availableRemoteServices) {
      this.availableRemoteServices.addAll(availableRemoteServices);
    }
    public RemoteService pickAvailableRemoteService() {
      return availableRemoteServices.get(0);
    }

    public synchronized void add(int allocNbNewThread) {
      allocatedThreadsCount += allocNbNewThread;
    }
    public synchronized void subOne() {
      allocatedThreadsCount--;
      if (allocatedThreadsCount < 0) {
        throw new IllegalStateException("allocatedThreadsCount = " + allocatedThreadsCount);
      }
    }
    public synchronized int count() {
      return allocatedThreadsCount;
    }
  }

  private final RemoteServicesThreads remoteServicesThreads = new RemoteServicesThreads();
  private final RequestsProvider requestsProvider;
  private final ResponseCollector responseCollector;
  private final AvailableRemoteServices availableRemoteServices;

  public boolean idle() {
    return remoteServicesThreads.count() == 0;
  }

  public void exec() {
    do {
      checkIfNewRemoteServicesAreAvailable();
      sleepForAWhile();
    } while(!requestsProvider.empty() || remoteServicesThreads.count() != 0);
  }

  private void checkIfNewRemoteServicesAreAvailable() {
    availableRemoteServices.update();
    if (availableRemoteServices.threadsCount() == 0) {
      log.warn("No remote services available, will wait...");
      return;
    }

    remoteServicesThreads.addAvailableRemoteServices(availableRemoteServices.services());
    allocateNewThreads(availableRemoteServices.threadsCount());
  }

  private void allocateNewThreads(int allocNbNewThread) {
    if (requestsProvider.empty()) {
      return;
    }
    int currentThreadsCount = remoteServicesThreads.count();
    log.info("Allocate {} new threads to process requests, total = {}", allocNbNewThread, currentThreadsCount + allocNbNewThread);
    remoteServicesThreads.add(allocNbNewThread);

    IntStream.range(0, allocNbNewThread).forEach(i -> {
              RemoteService remoteService = remoteServicesThreads.pickAvailableRemoteService();
              new Thread(run(remoteService)).start();
            }
    );
  }

  private Runnable run(RemoteService remoteService) {
    return () -> {
      // Optional is empty if no more request are pending (request provider is empty)
      ExecutorContext context = ExecutorContext.builder()
              .requestsProvider(requestsProvider)
              .responseCollector(responseCollector)
              .remoteService(remoteService)
              .threadId(Thread.currentThread().getId())
              .build();
      requestsProvider.getPendingRequestExecutor()
        .ifPresent(executor -> executor.exec(context));

      remoteServicesThreads.subOne();
    };
  }

  /** Let threads do their job, and check if new microservices are around or have disappeared */
  private void sleepForAWhile() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      log.warn("Waiting thread was interrupted: " + e.getMessage());
    }
  }
}
