package com.orange.documentare.core.comp.clustering.graph;
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
import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.comp.TriangleVertices;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputItem implements ClusteringItem {
  private int itemId;
  private String humanReadableId;
  private NearestItem[] nearestItems;
  private Integer clusterId;
  private Boolean clusterCenter;
  private byte[] bytes;

  /** reversed fileName to speed up string comparison during Ncd. Useful here to create a "visible" output debugEnabled directory */
  private String fileNameReversed;

  @Override
  public void setClusterCenter(boolean isCenter) {
    clusterCenter = isCenter;
  }

  @Override
  public boolean triangleVerticesAvailable() {
    return false;
  }

  @Override
  public TriangleVertices getTriangleVertices() {
    return null;
  }
}
