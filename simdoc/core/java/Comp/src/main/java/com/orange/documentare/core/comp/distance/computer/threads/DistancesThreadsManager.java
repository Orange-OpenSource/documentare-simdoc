package com.orange.documentare.core.comp.distance.computer.threads;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.distance.computer.DistancesComputer;
import com.orange.documentare.core.comp.measure.Progress;
import com.orange.documentare.core.comp.measure.ProgressListener;
import com.orange.documentare.core.comp.measure.TreatmentStep;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class DistancesThreadsManager {
  private final DistancesComputer computer;
  private final ExecutorService executor = getExecutorService();
  private final List<Future> futures = Collections.synchronizedList(new ArrayList<>());

  @Setter
  private ProgressListener listener;

  public void start() {
    int[] itemIndicesToProcess;
    while ((itemIndicesToProcess = computer.getItemsIndicesToProcess()) != null) {
      startForNextBasketElement(itemIndicesToProcess);
    }
    waitTheEnd();
    listener.onProgressUpdate(TreatmentStep.NCD, new Progress(100, computer.computeTimeSinceStartInSec()));
  }

  private void startForNextBasketElement(int[] itemIndicesToProcess) {
    DistanceItem[] items = computer.getItemsToProcess(itemIndicesToProcess);
    startTask(itemIndicesToProcess, items);
  }

  private void startTask(int[] itemIndicesToProcess, DistanceItem[] items) {
    Future future = executor.submit(new DistancesThreadRunnable(itemIndicesToProcess, items, computer, listener));
    futures.add(future);
  }

  private void waitTheEnd() {
    while(!futures.isEmpty()) {
      Future future = futures.get(0);
      waitForFuture(future);
      futures.remove(future);
    }
    executor.shutdown();
  }

  private void waitForFuture(Future future) {
    try {
      future.get();
    } catch (InterruptedException | ExecutionException e1) {
      showFutureError(e1);
    }
  }

  private void showFutureError(Exception e) {
    log.error(String.format("Error while waiting for the future: '%s'", e.getMessage()));
    e.printStackTrace();
  }

  private ExecutorService getExecutorService() {
    return Executors.newFixedThreadPool(getProcessors());
  }

  public static int getProcessors() {
    return Runtime.getRuntime().availableProcessors();
  }
}
