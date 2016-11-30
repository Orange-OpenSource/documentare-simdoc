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
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import lombok.RequiredArgsConstructor;

/** Work on all graph items, given triangles equilaterality & area statistics, it will exclude graph items */
@RequiredArgsConstructor
public class TriangleScissor {
  private final ClusteringGraph clusteringGraph;
  private final ClusteringParameters clusteringParameters;

  /**
   * @return number of "cut" singletons
   */
  public int cut() {
    TriangleScissorTrigger triangleScissorTrigger = new TriangleScissorTrigger(clusteringGraph.getItems(), clusteringParameters);
    int cutSingletonsCount = 0;
    for (GraphItem item : clusteringGraph.getItems()) {
      if (!item.isTriangleSingleton()) {
        cutSingletonsCount += toCutOrNotToCut(item, triangleScissorTrigger) ? 1 : 0;
      }
    }
    return cutSingletonsCount;
  }

  private boolean toCutOrNotToCut(GraphItem graphItem, TriangleScissorTrigger triangleScissorTrigger) {
    boolean shouldCut = triangleScissorTrigger.shouldCut(graphItem);
    graphItem.setTriangleSingleton(shouldCut);
    return shouldCut;
  }
}
