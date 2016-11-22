package com.orange.documentare.core.comp.clustering.graph.scissors;
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
import java.util.Comparator;

class NearestDistanceComparator implements Comparator<NearestItem> {
  @Override
  public int compare(NearestItem nearestItem1, NearestItem nearestItem2) {
    return Integer.compare(nearestItem1.getDistance(), nearestItem2.getDistance());
  }
}
