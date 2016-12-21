package com.orange.documentare.core.comp.image;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.ClusteringGraphBuilder;
import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.comp.distance.computer.DistancesComputer;
import com.orange.documentare.core.comp.measure.ProgressListener;
import com.orange.documentare.core.comp.measure.TreatmentStep;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import com.orange.documentare.core.system.measure.Progress;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DigitalTypesClustering implements ProgressListener {

  @Setter
  private ProgressListener progressListener = this;

  @Setter
  private boolean computeDistances = true;

  @Setter
  private boolean clearDistances = true;

  @Setter
  private boolean clearBytes = true;

  @Getter
  private Progress progress;

  public ClusteringGraph computeClusterIdsOf(DigitalTypes digitalTypes, ClusteringParameters clusteringParameters) {
    String thread = Thread.currentThread().toString();
    DigitalTypes itemsList = digitalTypes.copyWithoutSpaces();
    DigitalType[] items = itemsList.toArray(new DigitalType[itemsList.size()]);
    if (computeDistances) {
      log.info(thread + " Compute Ncd distances");
      computeNcdDistances(items, itemsList);
    }
    log.info(thread + " Compute Clustering");
    ClusteringGraph clusteringGraph = computeClustering(items, clusteringParameters);
    return clusteringGraph;
  }

  private void computeNcdDistances(DistanceItem[] items, DigitalTypes itemsList) {
    DistancesComputer computer = new DistancesComputer(items, items);
    computer.setProgressListener(progressListener);
    computer.compute();

    DistancesArray distancesArray = computer.getDistancesArray();
    for (int i = 0; i < items.length; i++) {
      DigitalType digitalType = (DigitalType)items[i];
      digitalType.setNearestItems(distancesArray.nearestItemsFor(itemsList, i));
      if (clearBytes) {
        digitalType.setBytes(null);
      }
    }
  }

  private ClusteringGraph computeClustering(ClusteringItem[] items, ClusteringParameters clusteringParameters) {
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    clusteringGraphBuilder.setProgressListener(progressListener);
    ClusteringGraph clusteringGraph = clusteringGraphBuilder.build(items, clusteringParameters);
    if (clearDistances) {
      for (int i = 0; i < items.length; i++) {
        ((DigitalType) items[i]).setNearestItems(null);
      }
    }
    return clusteringGraph;
  }

  @Override
  public void onProgressUpdate(TreatmentStep step, Progress progress) {
    this.progress = progress;
  }
}
