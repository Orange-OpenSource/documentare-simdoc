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
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor(suppressConstructorProperties = true)
class DistancesThreadRunnable implements Runnable {
  private final int[] itemIndices;
  private final DistanceItem[] items;
  private final DistancesComputer computer;
  private final ProgressListener listener;

  @Override
  public void run() {
    try {
      doRun();
    } catch (IOException e) {
      log.fatal(String.format("Fatal exception for input '%s': '%s'", items.toString(), e.getMessage()), e);
    }
  }

  private void doRun() throws IOException {
    for (int i = 0; i < itemIndices.length; i++) {
      computeDistancesFor(i);
      if (listener != null) {
        showProgress();
      }
    }
  }

  private void computeDistancesFor(int index) throws IOException {
    int startIndex = computer.isWorkingOnSameArray() ? itemIndices[index] + 1 : 0;
    int[] distancesToItem = computer.getDistance().get(items[index], computer.getItemsToCompareTo(), startIndex);
    computer.putResult(itemIndices[index], distancesToItem);
    onTaskFinishedEvent();
  }

  private void onTaskFinishedEvent() {
    computer.incTasksFinished();
  }

  private void showProgress() {
    listener.onProgressUpdate(TreatmentStep.NCD, new Progress(computeProgressPercent(), computer.computeTimeSinceStartInSec()));
  }

  private int computeProgressPercent() {
    int initialSize = computer.getItemsNumber();
    return 100 - (initialSize - computer.getTasksFinishedCount()) * 100 / initialSize;
  }
}
