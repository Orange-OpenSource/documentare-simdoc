package com.orange.documentare.core.comp.distance;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.comp.DistanceItem;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor(suppressConstructorProperties = true)
class DistanceIndexComparator implements Comparator<Integer> {
  private final List<? extends DistanceItem> items;
  private final int[] distances;
  private final int forItemIndex;

  @Override
  /** If distances are equals, we make sure that item index will come first */
  public int compare(Integer index1, Integer index2) {
    int cmp = Integer.compare(distances[index1], distances[index2]);
    return cmp != 0 ? cmp :
            index1 == forItemIndex ? -1 :
                    index2 == forItemIndex ? 1 :
                            items.get(index1).getHumanReadableId().compareTo(items.get(index2).getHumanReadableId());
  }
}
