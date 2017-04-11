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
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ClusteringGraph {
  private final List<GraphItem> items;
  private final Map<Integer, SubGraph> subGraphs = new HashMap<>();
  private final Map<Integer, GraphCluster> clusters = new HashMap<>();
}
