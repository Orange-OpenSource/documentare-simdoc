package com.orange.documentare.core.comp.clustering;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;

import java.util.HashMap;
import java.util.Map;

public class ClustersSize {

  private final DigitalTypes digitalTypes;

  /** map a cluster id to the cluster size */
  private final Map<Integer, Integer> clustersSize = new HashMap<>();

  public ClustersSize(DigitalTypes digitalTypes) {
    this.digitalTypes = digitalTypes;
    init();
  }

  /**
   * @param clusterId
   * @return the size of the cluster
   */
  public int sizeOf(int clusterId) {
    return clustersSize.get(clusterId);
  }

  /**
   * @param digitalTypeIndex
   * @return the size of the cluster
   */
  public int clusterSizeOf(int digitalTypeIndex) {
    return clustersSize.get(digitalTypes.get(digitalTypeIndex).getClusterId());
  }

  private void init() {
    digitalTypes.stream()
            .filter(digitalType -> !digitalType.isSpace())
            .forEach(digitalType -> addDigitalType(digitalType));
  }

  private void addDigitalType(DigitalType digitalType) {
    int clusterId = digitalType.getClusterId();
    int currentClusterSize = clustersSize.computeIfAbsent(clusterId, key -> 0);
    int newClusterSize = currentClusterSize + 1;
    clustersSize.put(clusterId, newClusterSize);
  }
}
