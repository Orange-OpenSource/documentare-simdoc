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

import com.orange.documentare.core.comp.clustering.graph.scissors.GraphScissor;
import com.orange.documentare.core.comp.clustering.graph.scissors.ScissorTrigger;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.SubGraph;

import java.util.Collection;

public class NonMinimalEdgesScissor extends GraphScissor {

  private final ScissorTrigger trigger;

  public NonMinimalEdgesScissor(ClusteringGraph clusteringGraph, Collection<SubGraph> subgraphs) {
    super(clusteringGraph, subgraphs);
    trigger = new NonMinimalEdgesTrigger();
  }

  @Override
  protected ScissorTrigger getScissorTrigger() {
    return trigger;
  }
}
