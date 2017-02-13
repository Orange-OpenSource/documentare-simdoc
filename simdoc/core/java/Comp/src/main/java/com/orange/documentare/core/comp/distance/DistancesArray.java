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
import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.segmentation.NearestItemsProvider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
public final class DistancesArray implements NearestItemsProvider {

  /** we expose these data in order to be able to serialize it in NCD */
  private int[][] distancesArray;
  private boolean onSameArray;
  private int nbColumns;

  public DistancesArray(int nbRows, int nbColumns, boolean onSameArray) {
    this.nbColumns = nbColumns;
    this.onSameArray = onSameArray;
    distancesArray = new int[nbRows][nbColumns];
  }

  public synchronized void setDistancesForItem(int itemIndex, int[] distancesForItem) {
    distancesArray[itemIndex] = distancesForItem;
  }

  public int[] getDistancesFor(int itemIndex) {
    return onSameArray ? getDistancesOnSameArrayFor(itemIndex) : distancesArray[itemIndex];
  }

  public int get(int rowIndex, int columnIndex) {
    return onSameArray ? getOnSameArray(rowIndex, columnIndex) : distancesArray[rowIndex][columnIndex];
  }

  public NearestItem nearestItemOf(int itemIndex) {
    return nearestItemOfBut(itemIndex, -1);
  }

  public NearestItem nearestItemOfBut(int itemIndex, int excludeIndex) {
    int minDistance = Integer.MAX_VALUE;
    int nearestIndex = -1;
    for (int i = 0; i < nbColumns; i++) {
      int distance = get(itemIndex, i);
      if (distance < minDistance && i != itemIndex && i != excludeIndex) {
        minDistance = distance;
        nearestIndex = i;
      }
    }
    return new NearestItem(nearestIndex, minDistance);
  }

  @Override
  public NearestItem[] nearestItemsFor(List<? extends DistanceItem> items, int itemIndex) {
    List<Integer> nearestIndices = getSortedByDistanceIndicesFor(items, itemIndex);
    NearestItem[] nearestItems = new NearestItem[nearestIndices.size()];
    fillNearestItems(itemIndex, nearestItems, nearestIndices);
    return nearestItems;
  }

  private void fillNearestItems(int itemIndex, NearestItem[] nearestItems, List<Integer> nearestIndices) {
    for (int i = 0; i < nearestItems.length; i++) {
      int nearestIndex = nearestIndices.get(i);
      nearestItems[i] = new NearestItem(nearestIndex, get(itemIndex, nearestIndex));
    }
  }

  private List<Integer> getSortedByDistanceIndicesFor(List<? extends DistanceItem> items, int itemIndex) {
    List<Integer> indices = getIndices();
    Collections.sort(indices, new DistanceIndexComparator(items, getDistancesFor(itemIndex), itemIndex));
    return indices;
  }

  private List<Integer> getIndices() {
    Integer[] indices = new Integer[nbColumns];
    for(int i = 0; i < indices.length; i++) {
      indices[i] = i;
    }
    return Arrays.asList(indices);
  }

  /**
   * The distance array is a square, and rows&columns elements are the same.
   * Here we implement the "half-matrix" optimization: d[i][j] = d[j][i]
   * @param rowIndex
   * @param columnIndex
   * @return distance
   */
  private int getOnSameArray(int rowIndex, int columnIndex) {
    if (rowIndex == columnIndex) {
      return 0;
    } else if (rowIndex < columnIndex) {
      // -1 : the '0' in the median is not in the array
      return distancesArray[rowIndex][columnIndex-rowIndex-1];
    } else {
      return distancesArray[columnIndex][rowIndex-columnIndex-1];
    }
  }

  /**
   * the "half-matrix" optimization let's us compute half of the matrix, but
   * here we need to generate the row as a whole.
   * @param itemIndex
   * @return array row, which all distances
   */
  private int[] getDistancesOnSameArrayFor(int itemIndex) {
    int[] row = new int[nbColumns];
    for (int i = 0; i < nbColumns; i++) {
      row[i] = getOnSameArray(itemIndex, i);
    }
    return row;
  }
}
