package com.orange.documentare.core.comp.clustering.graph.subgraphs;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.jgrapht.SameGroup;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;

public class SameSubgraph implements SameGroup {
  @Override
  public boolean areInSameGroup(GraphItem item1, GraphItem item2) {
    return item1.getSubgraphId() == item2.getSubgraphId();
  }

  @Override
  public int getGroupId(GraphItem item) {
    return item.getSubgraphId();
  }
}
