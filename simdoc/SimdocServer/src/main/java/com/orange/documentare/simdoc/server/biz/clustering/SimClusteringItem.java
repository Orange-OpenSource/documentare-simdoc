package com.orange.documentare.simdoc.server.biz.clustering;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.comp.TriangleVertices;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimClusteringItem implements ClusteringItem, DistanceItem {
  /** Keep the relative path name, and useful for items comparison when distance are equal */
  private final String humanReadableId;
  private TriangleVertices triangleVertices;
  private Integer clusterId;
  private boolean clusterCenter;

  SimClusteringItem(String relativeFilename) {
    // FIXME graphviz exception here: remove leading '/', graphviz hates it
    humanReadableId = relativeFilename.substring(1);
  }

  @Override
  public boolean triangleVerticesAvailable() {
    return true;
  }

  /** Not used */
  @Override
  public NearestItem[] getNearestItems() {
    return null;
  }
  @Override
  public byte[] getBytes() {
    return null;
  }
}
