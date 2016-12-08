package com.orange.documentare.app.prepclustering;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.comp.TriangleVertices;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
class NcdItem implements ClusteringItem, DistanceItem {
  /** file name with directory hierarchy below provided root directory */
  String relativeFilename;

  private String humanReadableId;
  private NearestItem[] nearestItems;
  private TriangleVertices triangleVertices;

  void updateHumanReadableId() {
    int filenameStart = relativeFilename.lastIndexOf("/");
    this.humanReadableId = relativeFilename.substring(filenameStart + 1);
    relativeFilename = null;
  }


  /** not used here */

  @Override
  public Integer getClusterId() {
    return null;
  }
  @Override
  public void setClusterId(Integer integer) {
  }

  @Override
  public void setClusterCenter(boolean b) {
  }
  @Override
  public boolean triangleVerticesAvailable() {
    return false;
  }
  @Override
  public byte[] getBytes() {
    return null;
  }
}
