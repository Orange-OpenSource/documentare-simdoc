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
import feign.Headers;
import feign.RequestLine;

import java.util.ArrayList;
import java.util.List;
import com.orange.documentare.app.ncdremote.MatrixDistancesSegments.MatrixDistancesSegment;

public class ResponseCollectorImpl implements ResponseCollector<MatrixDistancesSegment> {

  interface Distance {
    @RequestLine("POST /distances")
    @Headers("Content-Type: application/json")
    DistancesRequestResult distance(MatrixDistancesSegment segment);
  }

  private final List<MatrixDistancesSegment> segments = new ArrayList<>();

  @Override
  public synchronized void add(MatrixDistancesSegment matrixDistancesSegment) {
    segments.add(matrixDistancesSegment);
  }

  @Override
  public synchronized List<MatrixDistancesSegment> responses() {
    return ImmutableList.copyOf(segments);
  }
}
