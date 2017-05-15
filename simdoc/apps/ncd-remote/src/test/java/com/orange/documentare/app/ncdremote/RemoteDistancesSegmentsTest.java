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

import com.orange.documentare.app.ncdremote.MatrixDistancesSegments.MatrixDistancesSegment;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.List;

public class RemoteDistancesSegmentsTest {

  @Test

  public void remote_computation_on_distinct_elements_arrays() {
    // Given
    MatrixDistancesSegments matrixDistancesSegments = new MatrixDistancesSegments(TestElements.elements1(), TestElements.elements2());
    matrixDistancesSegments = matrixDistancesSegments.buildSegments();

    RemoteDistancesSegments remoteDistancesSegments =
            new RemoteDistancesSegments(matrixDistancesSegments);

    // When
    List<MatrixDistancesSegment> segments = remoteDistancesSegments.compute();

    // Then
    Assertions.assertThat(segments.get(0).distances).isEqualTo(new int[] {0});
  }
}
