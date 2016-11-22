package com.orange.documentare.core.model.ref.comp;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TriangleVertices {
  private NearestItem vertex2;
  private NearestItem vertex3;
  private int edge13;


  public TriangleVertices(ClusteringItem clusteringItem, ClusteringItem[] items) {
    NearestItem[] vertex1Nearest = clusteringItem.getNearestItems();
    this.vertex2 = searchVertex2(vertex1Nearest);

    NearestItem[] vertex2Nearest = items[vertex2.getIndex()].getNearestItems();
    this.vertex3 = searchVertex3(vertex1Nearest, vertex2Nearest);

    this.edge13 = searchEdge13(vertex1Nearest, vertex3.getIndex());
  }

  @JsonIgnore
  public int getEdge12() {
    return vertex2.getDistance();
  }
  @JsonIgnore
  public int getEdge23() {
    return vertex3.getDistance();
  }

  public static NearestItem searchVertex2(NearestItem[] vertex1Nearest) {
    return vertex1Nearest[1];
  }

  private NearestItem searchVertex3(NearestItem[] vertex1Nearest, NearestItem[] vertex2Nearest) {
    NearestItem vertex1 = vertex1Nearest[0];
    NearestItem vertex3 = vertex2Nearest[1];
    return vertex3.getIndex() == vertex1.getIndex() ? vertex2Nearest[2] : vertex3;
  }

  private int searchEdge13(NearestItem[] nearestItemsVertex1, int vertex3Index) {
    for (NearestItem item : nearestItemsVertex1) {
      if (item.getIndex() == vertex3Index) {
        return item.getDistance();
      }
    }
    throw new IllegalStateException("Failed to find searchVertex2 item");
  }
}
