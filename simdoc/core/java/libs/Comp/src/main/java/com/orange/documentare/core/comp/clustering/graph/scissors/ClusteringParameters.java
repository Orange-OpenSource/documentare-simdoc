package com.orange.documentare.core.comp.clustering.graph.scissors;
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
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClusteringParameters {
  private float stdQFactor = 2;
  private float stdAreaFactor = 2;
  private float stdSubgraphDistanceFactor = 2;
  private int distClusterThreshPercentile = 75;

  private boolean cutNonMinimalVerticesEnabled;
  private boolean cutSubgraphLongestVerticesEnabled;
  private boolean cutClusterLongestVerticesEnabled;
}
