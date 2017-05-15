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
import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class RemoteDistancesSegmentsTest {

  private final TestAnimalsElements testAnimalsElements = new TestAnimalsElements();

  @Test
  public void remote_computation_on_distinct_elements_arrays() throws IOException {
    // Given
    // FIXME: do not use same array trick, first try without it
    NcdOutputImporter ncdOutputImporter = readReference();

    BytesData[] elements1 = testAnimalsElements.elements();
    BytesData[] elements2 = testAnimalsElements.elements();
    MatrixDistancesSegments matrixDistancesSegments = new MatrixDistancesSegments(elements1, elements2);
    matrixDistancesSegments = matrixDistancesSegments.buildSegments();

    RemoteDistancesSegments remoteDistancesSegments =
            new RemoteDistancesSegments(matrixDistancesSegments);

    // When
    List<MatrixDistancesSegment> segments = remoteDistancesSegments.compute();

    // Then
    segments.stream().forEach(segment -> doAssertion(segment, ncdOutputImporter));
  }

  private void doAssertion(MatrixDistancesSegment segment, NcdOutputImporter ncdOutputImporter) {
    String id = new File(segment.element.filepath).getName();
    int refIndex = IntStream.range(0, ncdOutputImporter.items1.length)
            .filter(index -> ncdOutputImporter.items1[index].relativeFilename.equals(id))
            .findFirst()
            .getAsInt();
    Assertions.assertThat(segment.distances).isEqualTo(ncdOutputImporter.distancesArray.getDistancesFor(refIndex));
  }

  private NcdOutputImporter readReference() throws IOException {
    File file = new File(getClass().getResource("/animals-dna-ncd_regular_files_model.json.gz").getFile());
    return (NcdOutputImporter) (new JsonGenericHandler()).getObjectFromJsonGzipFile(NcdOutputImporter.class, file);
  }
}
