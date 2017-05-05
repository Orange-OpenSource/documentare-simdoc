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
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RemoteDistancesSegments {
  private final MatrixDistancesSegments matrixDistancesSegments;
  private final AvailableRemoteServices availableRemoteServices;

  public RemoteDistancesSegments compute() {
    return null;
  }

  public ImmutableList<MatrixDistancesSegment> segments() {
    return matrixDistancesSegments.segments;
  }
}
