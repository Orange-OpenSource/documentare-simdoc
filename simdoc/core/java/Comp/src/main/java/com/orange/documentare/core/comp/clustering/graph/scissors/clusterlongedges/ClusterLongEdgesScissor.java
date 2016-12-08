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
import com.orange.documentare.core.comp.clustering.graph.scissors.longedges.LongEdgesScissor;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphGroup;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;
import java.util.List;

@Log4j2
public class ClusterLongEdgesScissor extends LongEdgesScissor {

  public ClusterLongEdgesScissor(ClusteringGraph clusteringGraph, Collection<? extends GraphGroup> groups, float distanceThreshPercentile) {
    super(clusteringGraph, groups, distanceThreshPercentile);
  }

  @Override
  protected ScissorTrigger getTriggerImpl(List<GraphItem> graphItems, float distanceThreshPercentile) {
    return new ClusterLongEdgesTrigger(graphItems, distanceThreshPercentile);
  }
}
