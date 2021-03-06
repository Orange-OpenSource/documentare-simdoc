package com.orange.documentare.core.comp.clustering.graph.trianglesingleton;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.core.comp.clustering.stats.Stats;
import com.orange.documentare.core.comp.clustering.stats.TrianglesStats;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter(AccessLevel.PACKAGE)
class TriangleScissorTrigger {
  private final boolean acut;
  private final boolean qcut;
  private final float qThreshold;
  private final float areaThreshold;

  TriangleScissorTrigger(List<GraphItem> items, ClusteringParameters clusteringParameters) {
    acut = clusteringParameters.acut();
    qcut = clusteringParameters.qcut();
    TrianglesStats trianglesStats = getTriangleStats(items);
    qThreshold = getQCutThreshold(trianglesStats.getTrianglesEquilaterality(), clusteringParameters.qcutSdFactor);
    areaThreshold = getAreaThreshold(trianglesStats.getTrianglesArea(), clusteringParameters.acutSdFactor);
  }

  boolean shouldCut(GraphItem graphItem) {
    boolean shouldCut =
            (qcut && graphItem.getQ() < qThreshold) || (acut && graphItem.getArea() > areaThreshold);

    if (shouldCut && log.isDebugEnabled()) {
      log.debug(String.format("Cut item %s (q %f - a %f - triangulation qThresh %f aThresh %f)", graphItem.getVertexName(), graphItem.getQ(), graphItem.getArea(), qThreshold, areaThreshold));
    }
    return shouldCut;
  }

  private float getQCutThreshold(Stats trianglesEquilaterality, float qcutSdFactor) {
    return (float) (trianglesEquilaterality.getMean() - qcutSdFactor * trianglesEquilaterality.getStandardDeviation());
  }

  private float getAreaThreshold(Stats trianglesArea, float acutSdFactor) {
    return (float) (trianglesArea.getMean() + acutSdFactor * trianglesArea.getStandardDeviation());
  }

  private TrianglesStats getTriangleStats(List<GraphItem> items) {
    Stats qStats = new Stats();
    Stats areaStats = new Stats();
    for (GraphItem item : items) {
      if (!item.isTriangleSingleton()) {
        qStats.addValue(item.getQ());
        areaStats.addValue(item.getArea());
      }
    }
    return new TrianglesStats(qStats, areaStats);
  }
}
