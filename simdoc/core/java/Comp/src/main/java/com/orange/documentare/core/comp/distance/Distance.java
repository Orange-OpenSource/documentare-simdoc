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
import com.orange.documentare.core.comp.ncd.NcdResult;
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

  private final Ncd ncd = new Ncd();

  final Map<DistanceItem, NcdInput> inputsMap = new HashMap<>();

  /**
   * Compute distances of items1 to items2
   * @param items1
   * @param items2
   * @return Distances of items1 against items2
   */
  public DistancesArray compute(DistanceItem[] items1, DistanceItem[] items2) {
    DistancesComputer computer = new DistancesComputer(items1, items2);
    computer.compute();
    return computer.getDistancesArray();
  }

  public int[] compute(DistanceItem item, DistanceItem[] items) throws IOException {
    return compute(item, items, 0);
  }

  public int[] compute(DistanceItem item, DistanceItem[] items, int startIndex) throws IOException {
    int[] array = new int[items.length-startIndex];
    for (int i = startIndex; i < items.length; i++) {
      DistanceItem arrayItem = items[i];
      array[i-startIndex] = item.equals(arrayItem) ? 0 : compute(item, arrayItem);
    }
    return array;
  }

  public int compute(DistanceItem item1, DistanceItem item2) throws IOException {
    NcdInput input1 = ncdInputFor(item1);
    NcdInput input2 = ncdInputFor(item2);
    NcdResult ncdResult = ncd.computeNcd(input1, input2);
    updateCacheMap(item1, input1, ncdResult.input1CompressedLength);
    updateCacheMap(item2, input2, ncdResult.input2CompressedLength);
    float distance = ncdResult.ncd;
    return (int) (distance * DISTANCE_INT_CONV_FACTOR);
  }

  private synchronized NcdInput ncdInputFor(DistanceItem item) {
    NcdInput ncdInput = inputsMap.get(item);
    if (ncdInput == null) {
      ncdInput = new NcdInput(item.getBytes());
      inputsMap.put(item, ncdInput);
    }
    return ncdInput;
  }

  private synchronized void updateCacheMap(DistanceItem distanceItem, NcdInput ncdInput, int compressedLength) {
    inputsMap.put(distanceItem, ncdInput.withCompression(compressedLength));
  }
}
