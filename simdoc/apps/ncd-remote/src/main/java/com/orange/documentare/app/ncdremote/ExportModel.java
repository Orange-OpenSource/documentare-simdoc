package com.orange.documentare.app.ncdremote;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import com.orange.documentare.app.ncdremote.MatrixDistancesSegments.MatrixDistancesSegment;

@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
public class ExportModel {

  @RequiredArgsConstructor
  @EqualsAndHashCode
  public static class Item {
    public final String relativeFilename;

    public Item() {
      relativeFilename = null;
    }
  }

  public final Item[] items1;
  public final Item[] items2;
  public final DistancesArray distancesArray;

  public ExportModel() {
    items1 = null;
    items2 = null;
    distancesArray = null;
  }

  public ExportModel(BytesData[] bytesData1, BytesData[] bytesData2, List<MatrixDistancesSegments.MatrixDistancesSegment> segments) {
    boolean sameArray = bytesData1 == bytesData2;
    this.items1 = buildItems(bytesData1);
    this.items2 = sameArray ? null : buildItems(bytesData2);
    this.distancesArray = buildDistancesArray(items1.length, sameArray ? items1.length : items2.length, sameArray, segments);
  }

  private DistancesArray buildDistancesArray(int rows, int columns, boolean sameArray, List<MatrixDistancesSegment> segments) {
    List<MatrixDistancesSegment> sortedSegments = sort(segments);
    DistancesArray distancesArray = new DistancesArray(rows, columns, sameArray);
    IntStream.range(0, segments.size()).forEach(index -> {
      distancesArray.setDistancesForItem(index, sortedSegments.get(index).distances);
    });
    return distancesArray;
  }

  private List<MatrixDistancesSegment> sort(List<MatrixDistancesSegment> segments) {
    List<MatrixDistancesSegment> sortedSegments = new ArrayList<>(segments);
    sortedSegments.sort(Comparator.comparing(s -> s.element.id));
    return sortedSegments;
  }

  private Item[] buildItems(BytesData[] bytesDataArray) {
    return Arrays.stream(bytesDataArray)
      .map(bytesData -> bytesData.id)
      .map(Item::new)
      .toArray(size -> new Item[size]);
  }
}
