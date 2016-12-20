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

import com.orange.documentare.core.comp.clustering.graph.scissors.GraphScissor;
import com.orange.documentare.core.comp.clustering.graph.scissors.ScissorTrigger;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphGroup;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;

@Slf4j
public class LongEdgesScissor extends GraphScissor {
  private final ScissorTrigger trigger;

  public LongEdgesScissor(ClusteringGraph clusteringGraph, Collection<? extends GraphGroup> groups, float standardDeviationDistanceFactor) {
    super(clusteringGraph, groups);
    trigger = getTriggerImpl(clusteringGraph.getItems(), standardDeviationDistanceFactor);
  }

  protected ScissorTrigger getTriggerImpl(List<GraphItem> graphItems, float standardDeviationDistanceFactor) {
    return new LongEdgesTrigger(graphItems, standardDeviationDistanceFactor);
  }

  @Override
  protected ScissorTrigger getScissorTrigger() {
    return trigger;
  }
}
