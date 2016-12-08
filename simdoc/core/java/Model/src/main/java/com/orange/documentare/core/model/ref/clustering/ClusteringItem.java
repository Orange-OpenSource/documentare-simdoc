package com.orange.documentare.core.model.ref.clustering;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.comp.TriangleVertices;

public interface ClusteringItem {
  Integer getClusterId();
  void setClusterId(Integer id);

  void setClusterCenter(boolean isCenter);

  NearestItem[] getNearestItems();

  boolean triangleVerticesAvailable();
  TriangleVertices getTriangleVertices();

  String getHumanReadableId();

  byte[] getBytes();
}
