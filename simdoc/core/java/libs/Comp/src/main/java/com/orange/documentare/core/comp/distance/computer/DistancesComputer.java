package com.orange.documentare.core.comp.distance.computer;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.google.common.primitives.Ints;
import com.orange.documentare.core.comp.distance.Distance;
import com.orange.documentare.core.comp.distance.computer.threads.DistancesThreadsManager;
import com.orange.documentare.core.comp.measure.ProgressListener;
import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
/** Compute distances between two sets of items. Can not be recycled for a second use. */
public class DistancesComputer {
  private final DistanceItem[] items;
  private final DistancesThreadsManager distancesThreadsManager = new DistancesThreadsManager(this);

  @Getter
  /** Keep distance object common to all threads since it contains the cache of inputs! */
  private final Distance distance = new Distance();

  @Getter
  private final DistancesArray distancesArray;
  @Getter
  private final DistanceItem[] itemsToCompareTo;
  private final int linesPerThread;

  @Getter
  private int tasksFinishedCount;

  private int itemsIndex;
  private int itemsDone;
  private int t0Sec;

  public DistancesComputer(DistanceItem[] items1, DistanceItem[] items2) {
    items = items1;
    itemsToCompareTo = items2;
    distancesArray = new DistancesArray(items1.length, items2.length, isWorkingOnSameArray());
    linesPerThread = Math.max(items.length / DistancesThreadsManager.getProcessors(), 1);
  }

  public boolean isWorkingOnSameArray() {
    return items.equals(itemsToCompareTo);
  }

  public void compute() {
    t0Sec = getTimeSec();
    distancesThreadsManager.start();
  }

  /** @return item indices to process or null if we are done */
  public synchronized int[] getItemsIndicesToProcess() {
    List<Integer> indices = new ArrayList<>();
    for (int i = 0; i < Math.max(linesPerThread/2, 1); i++) {
      int[] pair = tryToGetPair();
      if (pair != null) {
        indices.add(pair[0]);
        if (pair.length == 2) {
          indices.add(pair[1]);
        }
      } else {
        break;
      }
    }
    return indices.isEmpty() ? null : Ints.toArray(indices);
  }

  private int[] tryToGetPair() {
    if (itemsDone == items.length) {
      return null;
    } else if (itemsDone == items.length - 1) {
      itemsDone = items.length;
      return new int[] { itemsIndex };
    } else {
      int currentIndex = itemsIndex++;
      itemsDone += 2;
      return new int[] { currentIndex, items.length - 1 - currentIndex };
    }
  }

  public DistanceItem[] getItemsToProcess(int[] itemIndexToProcess) {
    DistanceItem[] distanceItems = new DistanceItem[itemIndexToProcess.length];
    for(int i = 0; i < distanceItems.length; i++) {
      distanceItems[i] = items[itemIndexToProcess[i]];
    }
    return distanceItems;
  }

  public void putResult(int itemIndex, int[] distancesForItem) {
    distancesArray.setDistancesForItem(itemIndex, distancesForItem);
  }

  public void incTasksFinished() {
    tasksFinishedCount++;
  }

  public int computeTimeSinceStartInSec() {
    return getTimeSec() - t0Sec;
  }

  private int getTimeSec() {
    return (int)System.currentTimeMillis()/1000;
  }

  public void setProgressListener(ProgressListener listener) {
    distancesThreadsManager.setListener(listener);
  }

  public int getItemsNumber() {
    return items.length;
  }
}
