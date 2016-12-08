package com.orange.documentare.core.model.ref.clustering.graph;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
/** A group of items. This group is included in a cluster and in a subgraph, but is a cluster or a subgraph */
public abstract class GraphGroup {

  /** indices of contained items */
  private final List<Integer> itemIndices = new ArrayList<>();

  /** contained edges */
  private final List<GraphEdge> edges = new ArrayList<>();

  private int groupId = -1;
}
