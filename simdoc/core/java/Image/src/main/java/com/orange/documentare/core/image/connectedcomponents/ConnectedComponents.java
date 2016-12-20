package com.orange.documentare.core.image.connectedcomponents;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.segmentation.SegmentationRect;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ConnectedComponents extends ArrayList<ConnectedComponent> {

  @RequiredArgsConstructor
  class MergeResult {
    final ConnectedComponent mergedRect;
    final int mergedCount;
  }

  private class OrderOnX implements Comparator<SegmentationRect> {
    @Override
    public int compare(SegmentationRect r1, SegmentationRect r2) {
      return r1.x() - r2.x();
    }
  }

  private class OrderOnYAxis implements Comparator<SegmentationRect> {
    @Override
    public int compare(SegmentationRect r1, SegmentationRect r2) {
      return r1.y() - r2.y();
    }
  }

  public ConnectedComponents() {
    super();
  }

  public ConnectedComponents(ConnectedComponents connectedComponents) {
    super(connectedComponents);
  }

  public void sortVertically() {
    Collections.sort(this, new OrderOnYAxis());
  }

  public void mergeDiacriticsAndEyes() {
    mergeOverlapped(new DiacriticAndEyeOverlap());
  }

  public void mergeOverlapped() {
    mergeOverlapped(new DefaultOverlapImpl());
  }

  private void mergeOverlapped(Overlap overlap) {
    Collections.sort(this, new OrderOnX());
    ConnectedComponents merged = new ConnectedComponents();
    int index = 0;
    while (index < size()) {
      MergeResult result = findMergeSolutionInSublist(index, overlap);
      merged.add(result.mergedRect);
      index += result.mergedCount;
    }
    clear();
    addAll(merged);
  }

  private MergeResult findMergeSolutionInSublist(int index, Overlap overlap) {
    ConnectedComponent merged = get(index);
    int count = 1;
    while (index + count < size()) {
      SegmentationRect candidate = get(index + count);
      if (overlap.areOverlapping(merged, candidate)) {
        count++;
        merged.union(candidate);
      } else {
        break;
      }
    }
    return new MergeResult(merged, count);
  }
}