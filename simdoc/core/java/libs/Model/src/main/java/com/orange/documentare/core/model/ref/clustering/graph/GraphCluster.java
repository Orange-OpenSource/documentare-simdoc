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

/** Cluster built by running Voronoi'salgo on a subgraph */
@Setter
@Getter
public class GraphCluster extends GraphGroup {
  private int subgraphId;
}
