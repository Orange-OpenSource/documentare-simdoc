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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TriangleVertices {
  private NearestItem vertex2;
  private NearestItem vertex3;
  private Integer edge13;

  /** Based on the KNN criterion, if we did not found nearest items to build the triangle, then we declare this item as orphan */
  private Boolean orphan;

  public TriangleVertices(ClusteringItem clusteringItem, ClusteringItem[] items) {
    this(clusteringItem, items, items.length);
  }

  public TriangleVertices(ClusteringItem clusteringItem, ClusteringItem[] items, int k_nearest_neighbours_to_keep) {
    NearestItem[] vertex1Nearest = clusteringItem.getNearestItems();
    NearestItem v1Nearest = vertex1Nearest[1];

    NearestItem[] vertex2Nearest = items[v1Nearest.getIndex()].getNearestItems();
    NearestItem v2Nearest = searchVertex3(vertex1Nearest, vertex2Nearest);
    int edge13Candidate = searchEdge13(vertex1Nearest, v2Nearest.getIndex(), k_nearest_neighbours_to_keep);

    if (edge13Candidate < 0) {
      orphan = true;
    } else {
      this.vertex2 = v1Nearest;
      this.vertex3 = v2Nearest;
      this.edge13 = edge13Candidate;
    }
  }

  @JsonIgnore
  public int getEdge12() {
    return vertex2.getDistance();
  }
  @JsonIgnore
  public int getEdge23() {
    return vertex3.getDistance();
  }

  private NearestItem searchVertex3(NearestItem[] vertex1Nearest, NearestItem[] vertex2Nearest) {
    NearestItem vertex1 = vertex1Nearest[0];
    NearestItem vertex3 = vertex2Nearest[1];
    return vertex3.getIndex() == vertex1.getIndex() ? vertex2Nearest[2] : vertex3;
  }

  private int searchEdge13(NearestItem[] nearestItemsVertex1, int vertex3Index, int k_nearest_neighbours_to_keep) {
    Optional<NearestItem> v3 = Arrays.stream(nearestItemsVertex1)
            // + 1 since first nearest is item itself with null distance
            .limit(k_nearest_neighbours_to_keep + 1)
            .filter(nearestItem -> nearestItem.getIndex() == vertex3Index)
            .findAny();

    return v3.isPresent() ? v3.get().getDistance() : -1;
  }

  public boolean isOrphan() {
    return orphan != null && orphan;
  }
}
