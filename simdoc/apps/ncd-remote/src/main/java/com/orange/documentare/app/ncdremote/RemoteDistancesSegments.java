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
import com.orange.documentare.app.ncdremote.MatrixDistancesSegments.MatrixDistancesSegment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class RemoteDistancesSegments {
  private final List<MatrixDistancesSegment> segmentsToCompute;
  private final List<MatrixDistancesSegment> currentComputedSegments = new ArrayList<>();
  public final ImmutableList<MatrixDistancesSegment> computedSegments;

  public RemoteDistancesSegments(MatrixDistancesSegments matrixDistancesSegments) {
    this(new ArrayList<>(matrixDistancesSegments.segments), null);
  }

  private RemoteDistancesSegments(List<MatrixDistancesSegment> segmentsToCompute, ImmutableList<MatrixDistancesSegment> computedSegments) {
    this.segmentsToCompute = segmentsToCompute;
    this.computedSegments = computedSegments;
  }

  public RemoteDistancesSegments compute() {
    do {
      dispatchSegmentsToCompute();
    } while(!segmentsToCompute.isEmpty());

    return new RemoteDistancesSegments(segmentsToCompute, ImmutableList.copyOf(currentComputedSegments));
  }

  private void dispatchSegmentsToCompute() {

  }
}
