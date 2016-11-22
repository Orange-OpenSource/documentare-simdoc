package com.orange.documentare.core.comp.clustering.graph.jgrapht;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.clustering.graph.GraphEdge;
import com.orange.documentare.core.model.ref.clustering.graph.GraphGroup;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class GroupEdges {
  private final List<GraphItem> items;
  private final Map<Integer, ? extends GraphGroup> groups;
  private final SameGroup sameGroup;

  public void updateGroupsEdges(Collection<JGraphEdge> edges) {
    for (JGraphEdge edge : edges) {
      setEdgeGroup(edge);
    }
  }

  private void setEdgeGroup(JGraphEdge jGraphEdge) {
    GraphEdge edge = jGraphEdge.getEdge();
    GraphItem item1 = items.get(edge.getVertex1Index());
    GraphItem item2 = items.get(edge.getVertex2Index());
    if (sameGroup.areInSameGroup(item1, item2)) {
      groups.get(sameGroup.getGroupId(item1)).getEdges().add(edge);
    }
  }
}
