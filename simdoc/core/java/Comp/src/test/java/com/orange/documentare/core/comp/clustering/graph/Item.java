package com.orange.documentare.core.comp.clustering.graph;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Denis Boisset & Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.comp.TriangleVertices;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@RequiredArgsConstructor
public class Item implements ClusteringItem {

  public interface ItemInit {
    void init(Item[] items);
  }

  private int itemId;
  private final String humanReadableId;
  private Float nearestTriangleArea;
  private Integer clusterId;
  private Boolean clusterCenter;
  private NearestItem[] nearestItems;
  private byte[] bytes;

  private TriangleVertices triangleVertices;

  // FIXME: still used in real?
  /** reversed fileName to speed up string comparison during Ncd. Useful here to create a "visible" output debugEnabled directory */
  private String fileNameReversed;

  @Override
  public void setClusterCenter(boolean isCenter) {
    clusterCenter = isCenter;
  }

  @Override
  public boolean triangleVerticesAvailable() {
    return triangleVertices != null;
  }

  public static ClusteringItem[] buildClusteringItems(ItemInit itemInit, int nbItems) {
    Item[] items = buildItems(nbItems);
    itemInit.init(items);

    int knn = items.length;
    for (int i = 0; i < items.length; i++) {
      items[i].setTriangleVertices(new TriangleVertices(items[i], items, knn));
    }
    // Ensure we can only rely on vertices
    // FIXME: keep nearest for Jojo test and singletons regraph
    //Arrays.asList(items).stream().forEach(item -> item.setNearestItems(null));
    return items;
  }

  private static Item[] buildItems(int nbItems) {
    Item[] items = new Item[nbItems];
    for (int i = 0; i < items.length; i++) {
      items[i] = new Item(String.valueOf(i));
    }
    return items;
  }
}
