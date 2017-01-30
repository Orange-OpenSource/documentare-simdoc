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

import com.orange.documentare.core.comp.distance.computer.DistancesComputer;
import com.orange.documentare.core.comp.ncd.Ncd;
import com.orange.documentare.core.comp.ncd.NcdInput;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class Distance {

  public static final int DISTANCE_INT_CONV_FACTOR = 1000000;

  private final Map<DistanceItem, NcdInput> inputsMap = new HashMap<>();
  private final Ncd ncd = new Ncd();

  /**
   * Compute distances of items1 to items2
   * @param items1
   * @param items2
   * @return Distances of items1 against items2
   */
  public DistancesArray get(DistanceItem[] items1, DistanceItem[] items2) {
    DistancesComputer computer = new DistancesComputer(items1, items2);
    computer.compute();
    return computer.getDistancesArray();
  }

  public int[] get(DistanceItem item, DistanceItem[] items) throws IOException {
    return get(item, items, 0);
  }

  public int[] get(DistanceItem item, DistanceItem[] items, int startIndex) throws IOException {
    int[] array = new int[items.length-startIndex];
    for (int i = startIndex; i < items.length; i++) {
      DistanceItem arrayItem = items[i];
      array[i-startIndex] = item.equals(arrayItem) ? 0 : get(item, arrayItem);
    }
    return array;
  }

  public int get(DistanceItem item1, DistanceItem item2) throws IOException {
    NcdInput input1 = getNcdInput(item1);
    NcdInput input2 = getNcdInput(item2);
    // FIXME OPTIMIZATION: ncdInput is immutable and compressedLength should be updated in retained Map or array
    float distance = ncd.computeNcd(input1, input2).ncd;
    return (int) (distance * DISTANCE_INT_CONV_FACTOR);
  }

  private synchronized NcdInput getNcdInput(DistanceItem item) {
    // FIXME: should use getOrDefault
    NcdInput ncdInput = inputsMap.get(item);
    if (ncdInput == null) {
      ncdInput = new NcdInput(item.getBytes());
      inputsMap.put(item, ncdInput);
    }
    return ncdInput;
  }
}
