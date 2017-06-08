package com.orange.documentare.core.comp.clustering.graph;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Denis Boisset & Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.comp.TriangleVertices;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class SingletonForReGraph implements ClusteringItem {
  private Integer clusterId;
  private boolean clusterCenter;

  private final String humanReadableId;
  private final int originalItemIndex;
  private final NearestItem[] nearestItems;

  public SingletonForReGraph(ClusteringItem singleton, int originalSingletonIndex, Map<Integer, Integer> singletonsOldToNewIndexMap) {
    this.humanReadableId = singleton.getHumanReadableId();
    this.originalItemIndex = originalSingletonIndex;
    nearestItems = buildNearest(singleton.getNearestItems(), singletonsOldToNewIndexMap);
  }

  private NearestItem[] buildNearest(NearestItem[] nearestItems, Map<Integer, Integer> singletonsOldToNewIndexMap) {
    List<NearestItem> nearest = Arrays.stream(nearestItems)
      .filter(nearestItem -> singletonsOldToNewIndexMap.keySet().contains(nearestItem.getIndex()))
      .map(nearestItem -> new NearestItem(singletonsOldToNewIndexMap.get(nearestItem.getIndex()), nearestItem.getDistance()))
      .collect(Collectors.toList());
    return nearest.toArray(new NearestItem[nearest.size()]);
  }

  @Override
  public boolean triangleVerticesAvailable() {
    // not used
    return false;
  }
  @Override
  public TriangleVertices getTriangleVertices() {
    // not used
    return null;
  }
  @Override
  public byte[] getBytes() {
    // not used
    return null;
  }
}
