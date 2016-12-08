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

import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.comp.TriangleVertices;

import java.util.Arrays;
import java.util.List;

class ClusteringModel {
  public final NcdItem[] items;
  private final int kNearestNeighbours;

  ClusteringModel(RegularFilesDistances regularFilesDistances, int kNearestNeighbours) {
    this.items = regularFilesDistances.getItems1();

    this.kNearestNeighbours =
            kNearestNeighbours <= 0 ? items.length : kNearestNeighbours;

    DistancesArray distancesArray = regularFilesDistances.getDistancesArray();
    initWithTriangulationVertices(distancesArray);
  }

  /** Memory in place creation, it is optimal since we do not allocate nearest arrays */
  private void initWithTriangulationVertices(DistancesArray distancesArray) {
    List<NcdItem> itemsList = Arrays.asList(items);
    for (int i = 0; i < items.length; i++) {
      NearestItem vertex2 = distancesArray.nearestItemOf(i);
      NearestItem vertex3 = distancesArray.nearestItemOfBut(vertex2.getIndex(), i);
      items[i].setTriangleVertices(new TriangleVertices(distancesArray.nearestItemsFor(itemsList, i), vertex3, kNearestNeighbours));
    }
  }
}
