package com.orange.documentare.core.comp.clustering.graph.jgrapht.graphwriter;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.jgrapht.JGraphEdge;
import com.orange.documentare.core.comp.distance.Distance;
import org.jgrapht.ext.EdgeNameProvider;

public class EdgeLabelProvider implements EdgeNameProvider<JGraphEdge> {
  @Override
  public String getEdgeName(JGraphEdge edge) {
    return String.valueOf(edge.getEdge().getLength() * 1000 / Distance.DISTANCE_INT_CONV_FACTOR);
  }
}
