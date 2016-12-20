package com.orange.documentare.core.comp.distance.matrix;
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

@RequiredArgsConstructor(suppressConstructorProperties = true)
class DistanceByNameIndexComparator implements Comparator<Integer> {
  private final DistanceItem[] items;

  @Override
  public int compare(Integer i1, Integer i2) {
    String s1 = items[i1].getHumanReadableId();
    String s2 = items[i2].getHumanReadableId();
    return s1.compareTo(s2);
  }
}
