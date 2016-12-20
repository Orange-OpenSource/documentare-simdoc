package com.orange.documentare.core.comp.clustering.graph.scissors.longedges;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.scissors.ScissorTrigger;
import com.orange.documentare.core.comp.clustering.stats.Stats;
import com.orange.documentare.core.model.ref.clustering.graph.GraphEdge;
import com.orange.documentare.core.model.ref.clustering.graph.GraphGroup;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
class LongEdgesTrigger implements ScissorTrigger {

  private final List<GraphItem> graphItems;
  private final float standardDeviationDistanceFactor;

  @Getter(AccessLevel.PACKAGE)
  private int edgeLengthThreshold;

  @Override
  public void initForGroup(GraphGroup group) {
    edgeLengthThreshold = getEdgeLengthCutThreshold(group);
  }

  @Override
  public boolean shouldRemove(GraphEdge edge) {
    return edge.getLength() >= edgeLengthThreshold;
  }

  private int getEdgeLengthCutThreshold(GraphGroup group) {
    edgeLengthThreshold = Integer.MAX_VALUE;
    Stats stats = getStatisticsFor(group);
    edgeLengthThreshold = getEdgeLengthThreshold(stats);
    stats = getStatisticsFor(group);
    edgeLengthThreshold = getEdgeLengthThreshold(stats);
    debugGroupStatistics(group, stats);
    return edgeLengthThreshold;
  }

  private Stats getStatisticsFor(GraphGroup group) {
    Stats stats = new Stats();
    for (GraphEdge edge : group.getEdges()) {
      tryToAddEdge(stats, edge.getLength());
    }
    return stats;
  }

  private void tryToAddEdge(Stats stats, int edgeLength) {
    if (edgeLength <= edgeLengthThreshold) {
      stats.addValue(edgeLength);
    }
  }

  private int getEdgeLengthThreshold(Stats stats) {
    return (int) (stats.getMean() + standardDeviationDistanceFactor * stats.getStandardDeviation());
  }

  private void debugGroupStatistics(GraphGroup group, Stats stats) {
    log.debug("Stats for group {}, edges {}, mean = {}, min = {}, max = {}, std = {}", group.getGroupId(), group.getEdges().size(), stats.getMean(), stats.getMin(), stats.getMax(), stats.getStandardDeviation());
  }
}
