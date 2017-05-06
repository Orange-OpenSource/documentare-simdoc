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

import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
public class RequestsExecutor {

  class AllocatedThreads {
    private int allocatedThreadsCount;

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

  private final AllocatedThreads allocatedThreads = new AllocatedThreads();
  private final RequestsProvider requestsProvider;
  private final ResponseCollector responseCollector;
  private final AvailableRemoteServices availableRemoteServices;

  public void exec() {
    availableRemoteServices.update();
    allocateNewThreads(availableRemoteServices.threadsCount());

    do {
      sleepForAWhile();
      checkIfWeShouldAllocateNewThreads();
    } while(!requestsProvider.empty() || allocatedThreads.count() != 0);
  }

  private void allocateNewThreads(int allocNbNewThread) {
    if (requestsProvider.empty()) {
      return;
    }
    int currentThreadsCount = allocatedThreads.count();
    log.info("Allocate {} new threads to process requests, total = {}", allocNbNewThread, currentThreadsCount + allocNbNewThread);
    allocatedThreads.add(allocNbNewThread);
    IntStream.range(currentThreadsCount, currentThreadsCount + allocNbNewThread).forEach(threadId ->
            new Thread(run(threadId)).start()
    );
  }

  private Runnable run(int threadId) {
    return () -> {
      // Optional is empty if no more request are pending (request provider is empty)
      requestsProvider.getPendingRequestExecutor()
        .ifPresent(executor -> executor.exec(requestsProvider, responseCollector, threadId));

      allocatedThreads.subOne();
    };
  }

  private void checkIfWeShouldAllocateNewThreads() {
    availableRemoteServices.update();
    int delta = availableRemoteServices.threadsCount() - allocatedThreads.count();
    if (delta > 0) {
      allocateNewThreads(delta);
    }
    if (availableRemoteServices.threadsCount() == 0) {
      log.warn("No remote services available, will wait...");
    }
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
