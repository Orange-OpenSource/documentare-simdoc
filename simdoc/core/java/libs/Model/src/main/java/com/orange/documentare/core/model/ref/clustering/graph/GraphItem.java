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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class GraphItem {
  /** triangle first vertex */
  private String vertexName;

  /** no triangle due to K Nearest Neighbours criteria */
  private Boolean orphan;

  @JsonIgnore
  /** triangle first vertex */
  private ClusteringItem vertex1;
  @JsonIgnore
  /** triangle second vertex */
  private ClusteringItem vertex2;
  @JsonIgnore
  /** triangle third vertex */
  private ClusteringItem vertex3;

  /** triangle first vertex */
  private int vertex1Index;
  /** triangle second vertex */
  private int vertex2Index;
  /** triangle third vertex */
  private int vertex3Index;

  /** triangle area */
  private float area;

  /** triangle equilaterality factor */
  private float q;

  /** triangle edges length */
  private int[] edgesLength;

  /** given triangle equilaterality & area, indicates if we exclude this item as a singleton */
  private boolean triangleSingleton;

  private int subgraphId;
  private int clusterId;
  private boolean clusterCenter;

  @JsonIgnore
  public boolean isOrphan() {
    return orphan != null && orphan;
  }
}
