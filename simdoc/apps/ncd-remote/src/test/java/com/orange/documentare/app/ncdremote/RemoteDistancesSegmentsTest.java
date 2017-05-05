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
import org.fest.assertions.Assertions;
import org.junit.Test;
import com.orange.documentare.app.ncdremote.MatrixDistancesSegments.MatrixDistancesSegment;

public class RemoteDistancesSegmentsTest {

  @Test
  public void remote_computation_on_distinct_elements_arrays() {
    // Given
    MatrixDistancesSegments matrixDistancesSegments = new MatrixDistancesSegments(TestElements.elements1(), TestElements.elements2());
    matrixDistancesSegments = matrixDistancesSegments.buildSegments();

    AvailableRemoteService[] services = { new AvailableRemoteService("localhost:8080", 4) };
    AvailableRemoteServices availableRemoteServices = new AvailableRemoteServices(services);

    RemoteDistancesSegments remoteDistancesSegments =
            new RemoteDistancesSegments(matrixDistancesSegments, availableRemoteServices);

    // When
    remoteDistancesSegments = remoteDistancesSegments.compute();

    // Then
    ImmutableList<MatrixDistancesSegment> segments = remoteDistancesSegments.segments();
    Assertions.assertThat(segments.get(0).distances).isEqualTo(new int[] {0});
  }
}
