package com.orange.documentare.core.comp.clustering.graph.scissors.nonminimaledges;
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
import com.orange.documentare.core.model.ref.clustering.graph.GraphEdge;
import com.orange.documentare.core.model.ref.clustering.graph.GraphGroup;

import java.util.HashMap;
import java.util.Map;

class NonMinimalEdgesTrigger implements ScissorTrigger {
  /** Min edge length for a vertex (index) */
  private Map<Integer, Integer> vertexMinEdgeLength = new HashMap<>();

  @Override
  public void initForGroup(GraphGroup group) {
    for (GraphEdge edge : group.getEdges()) {
      checkVertexMinEdgeLength(edge);
    }
  }

  private void checkVertexMinEdgeLength(GraphEdge edge) {
    int vertexIndex = edge.getVertex1Index();
    Integer minEdgeLength = vertexMinEdgeLength.get(vertexIndex);
    minEdgeLength = minEdgeLength == null ? Integer.MAX_VALUE : minEdgeLength;
    int edgeLength = edge.getLength();
    if (edgeLength < minEdgeLength) {
      vertexMinEdgeLength.put(vertexIndex, edgeLength);
    }
  }

  @Override
  public boolean shouldRemove(GraphEdge edge) {
    return edge.getLength() != vertexMinEdgeLength.get(edge.getVertex1Index());
  }
}
