package com.orange.documentare.core.comp.clustering.graph.scissors.clusterlongedges;
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
class ClusterLongEdgesTrigger implements ScissorTrigger {
  private final List<GraphItem> graphItems;
  private final float thresholdPercentile;

  @Getter(AccessLevel.PACKAGE)
  private int edgeLengthThreshold;

  @Override
  public void initForGroup(GraphGroup group) {
    initCutThreshold(group);
  }

  @Override
  public boolean shouldRemove(GraphEdge edge) {
    return edge.getLength() > edgeLengthThreshold;
  }

  private void initCutThreshold(GraphGroup group) {
    Stats stats = new Stats();
    for (GraphEdge edge : group.getEdges()) {
      stats.addValue(edge.getLength());
    }
    edgeLengthThreshold = (int)stats.getPercentile(thresholdPercentile);
    debugStats(group, stats);
  }

  private void debugStats(GraphGroup group, Stats stats) {
    log.debug("Stats for group {}, edges = {}, threshold = {}, mean = {}, median = {}, min = {}, max = {}, sd = {}, mean+sd = {}", group.getGroupId(), group.getEdges().size(), edgeLengthThreshold, stats.getMean(), stats.getPercentile(50), stats.getMin(), stats.getMax(), stats.getStandardDeviation(), stats.getMean() + stats.getStandardDeviation());
  }
}
