package com.orange.documentare.core.comp.clustering.graph.subgraphs;
/*
 * Copyright (C) 2017 Orange
 * Authors: Christophe Maldivi, Joël Gardes
 *
 * This software is the confidential and proprietary information of Orange.
 * You shall not disclose such confidential information and shall use it only
 * in accordance with the terms of the license agreement you entered into
 * with Orange.
 */

import com.orange.documentare.core.comp.clustering.graph.scissors.GraphScissor;
import com.orange.documentare.core.comp.clustering.graph.scissors.ScissorTrigger;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphGroup;

import java.util.Collection;

public class InvalidEdgeDistanceScissor  extends GraphScissor {

  private final ScissorTrigger trigger;

  public InvalidEdgeDistanceScissor(ClusteringGraph clusteringGraph, Collection<? extends GraphGroup> groups) {
    super(clusteringGraph, groups);
    trigger = new InvalidEdgeDistanceTrigger();
  }

  @Override
  protected ScissorTrigger getScissorTrigger() {
    return trigger;
  }
}
