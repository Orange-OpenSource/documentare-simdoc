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
import lombok.Getter;
import org.jgrapht.graph.DefaultWeightedEdge;

@Getter
public class JGraphEdge extends DefaultWeightedEdge {
  private GraphEdge edge;

  public void init(int indexVertex1, int indexVertex2, int edgeLength) {
    edge = new GraphEdge(indexVertex1, indexVertex2, edgeLength);
  }
}
