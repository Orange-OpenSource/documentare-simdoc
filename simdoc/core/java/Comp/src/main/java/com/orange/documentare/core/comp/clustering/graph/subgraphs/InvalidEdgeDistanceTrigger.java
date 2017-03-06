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

import com.orange.documentare.core.comp.clustering.graph.scissors.ScissorTrigger;
import com.orange.documentare.core.comp.distance.Distance;
import com.orange.documentare.core.model.ref.clustering.graph.GraphEdge;
import com.orange.documentare.core.model.ref.clustering.graph.GraphGroup;

public class InvalidEdgeDistanceTrigger implements ScissorTrigger {

  @Override
  public boolean shouldRemove(GraphEdge edge) {
    return edge.getLength() >= Distance.DISTANCE_INT_CONV_FACTOR;
  }

  @Override
  public void initForGroup(GraphGroup group) {
    // not used
  }
}
