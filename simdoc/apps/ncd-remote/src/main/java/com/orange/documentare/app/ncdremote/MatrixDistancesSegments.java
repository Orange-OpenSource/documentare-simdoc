package com.orange.documentare.app.ncdremote;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.google.common.collect.ImmutableList;
import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** In current impl, a segment is basically a column of the matrix */
public class MatrixDistancesSegments {

  class MatrixDistancesSegment {
    /** element which will be compared (distance) to ... */
    public final BytesData element;
    /** ... compared (distance) to these elements */
    public final BytesData[] elements;
    public final int[] distances;

    public boolean distancesComputed() {
      return distances != null;
    }

    private MatrixDistancesSegment(BytesData element, BytesData[] elements) {
      this(element, elements, null);
    }

    private MatrixDistancesSegment(BytesData element, BytesData[] elements, int[] distances) {
      this.element = element;
      this.elements = elements;
      this.distances = distances;
    }
  }

  private final BytesData[] bytesData1;
  private final BytesData[] bytesData2;
  public final ImmutableList<MatrixDistancesSegment> segments;

  public MatrixDistancesSegments(BytesData[] bytesData1, BytesData[] bytesData2) {
    this(bytesData1, bytesData2, null);
  }

  private MatrixDistancesSegments(BytesData[] bytesData1, BytesData[] bytesData2, ImmutableList<MatrixDistancesSegment> segments) {
    this.bytesData1 = bytesData1;
    this.bytesData2 = bytesData2;
    this.segments = segments;
  }

  public MatrixDistancesSegments buildSegments() {
    ImmutableList<MatrixDistancesSegment> segs = doBuildSegments();
    return new MatrixDistancesSegments(bytesData1, bytesData2, segs);
  }

  private ImmutableList<MatrixDistancesSegment> doBuildSegments() {
    if (bytesData1 == bytesData2) {
      return buildSegmentsForSameElements();
    } else {
      return ImmutableList.copyOf(
              IntStream.range(0, bytesData1.length)
                      .mapToObj(i -> new MatrixDistancesSegment(bytesData1[i], bytesData2))
                      .collect(Collectors.toList())
      );
    }
  }

  private ImmutableList<MatrixDistancesSegment> buildSegmentsForSameElements() {
    return ImmutableList.copyOf(
            IntStream.range(0, bytesData1.length - 1)
                    .mapToObj(i -> new MatrixDistancesSegment(bytesData1[i], requiredComparisonElements(i)))
                    .collect(Collectors.toList())
    );
  }

  /**
   * Keep only required elements for element at index:
   *  - for same index, distance is null so do not keep it
   *  - for previous index, distance is already computed, so do not keep it
   **/
  private BytesData[] requiredComparisonElements(int index) {
    int nbToKeep = bytesData1.length - 1 - index;
    BytesData[] bytesData = new BytesData[nbToKeep];
    IntStream.range(0, nbToKeep)
            .forEach(i -> bytesData[i] = bytesData1[index + 1 + i]);
    return bytesData;
  }
}
