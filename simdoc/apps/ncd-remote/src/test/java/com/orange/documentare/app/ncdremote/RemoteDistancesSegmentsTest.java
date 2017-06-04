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
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class RemoteDistancesSegmentsTest {

  private final TestAnimalsElements testAnimalsElements = new TestAnimalsElements();

  @Test
  @Ignore // a running simdoc-server is mandatory for this test
  public void remote_computation_on_same_elements() throws IOException {
    // Given
    ExportModel referenceModel = readReferenceForSameArray();

    BytesData[] elements = testAnimalsElements.elements();
    MatrixDistancesSegments matrixDistancesSegments = new MatrixDistancesSegments(elements, elements);
    matrixDistancesSegments = matrixDistancesSegments.buildSegments();

    RemoteDistancesSegments remoteDistancesSegments =
            new RemoteDistancesSegments(matrixDistancesSegments);

    // When
    List<MatrixDistancesSegment> segments = remoteDistancesSegments.compute();

    // Then
    ExportModel exportModel = new ExportModel(elements, elements, segments);
    Assertions.assertThat(exportModel).isEqualTo(referenceModel);
  }

  @Test
  @Ignore // a running simdoc-server is mandatory for this test
  public void remote_computation_on_distinct_elements_arrays() throws IOException {
    // Given
    ExportModel referenceModel = readReferenceForDistinctArray();

    BytesData[] elements1 = testAnimalsElements.elements();
    BytesData[] elements2 = testAnimalsElements.elements();
    MatrixDistancesSegments matrixDistancesSegments = new MatrixDistancesSegments(elements1, elements2);
    matrixDistancesSegments = matrixDistancesSegments.buildSegments();

    RemoteDistancesSegments remoteDistancesSegments =
            new RemoteDistancesSegments(matrixDistancesSegments);

    // When
    List<MatrixDistancesSegment> segments = remoteDistancesSegments.compute();

    // Then
    segments.stream().forEach(segment -> doAssertion(segment, referenceModel));

    ExportModel exportModel = new ExportModel(elements1, elements2, segments);
    Assertions.assertThat(exportModel).isEqualTo(referenceModel);
  }

  private void doAssertion(MatrixDistancesSegment segment, ExportModel model) {
    String id = new File(segment.element.filepath).getName();
    int refIndex = IntStream.range(0, model.items1.length)
            .filter(index -> model.items1[index].relativeFilename.equals(id))
            .findFirst()
            .getAsInt();
    Assertions.assertThat(segment.distances).isEqualTo(model.distancesArray.getDistancesFor(refIndex));
  }

  private ExportModel readReferenceForSameArray() throws IOException {
    return readReference("/animals-dna-same-array-ncd_regular_files_model.json.gz");
  }

  private ExportModel readReferenceForDistinctArray() throws IOException {
    return readReference("/animals-dna-ncd_regular_files_model.json.gz");
  }

  private ExportModel readReference(String resId) throws IOException {
    File file = new File(getClass().getResource(resId).getFile());
    return (ExportModel) (new JsonGenericHandler()).getObjectFromJsonGzipFile(ExportModel.class, file);
  }
}
