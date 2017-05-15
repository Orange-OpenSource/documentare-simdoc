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

import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.stream.IntStream;
import com.orange.documentare.app.ncdremote.MatrixDistancesSegments.MatrixDistancesSegment;

public class MatrixDistancesSegmentsTest {

  @Test
  public void build_distances_matrix_segments_from_elements1_and_elements2() {
    // Given
    BytesData[] bytesData1 = TestElements.elements1();
    BytesData[] bytesData2 = TestElements.elements2();
    MatrixDistancesSegments matrixDistancesSegments = new MatrixDistancesSegments(bytesData1, bytesData2);

    // When
    MatrixDistancesSegments result = matrixDistancesSegments.buildSegments();

    // Then
    Assertions.assertThat(result.segments).hasSize(2);

    MatrixDistancesSegment segment1 = result.segments.get(0);
    MatrixDistancesSegment segment2 = result.segments.get(1);
    Assertions.assertThat(segment1.element.id).isEqualTo("1");
    Assertions.assertThat(segment1.elements[0].id).isEqualTo("4");
    Assertions.assertThat(segment1.elements[1].id).isEqualTo("1");
    Assertions.assertThat(segment1.elements[2].id).isEqualTo("5");

    Assertions.assertThat(segment2.element.id).isEqualTo("2");
    Assertions.assertThat(segment2.elements[0].id).isEqualTo("4");
    Assertions.assertThat(segment2.elements[1].id).isEqualTo("1");
    Assertions.assertThat(segment2.elements[2].id).isEqualTo("5");
  }

  @Test
  public void build_half_matrix_segments_if_elements1_equals_elements2() {
    // Given
    BytesData[] bytesData1 = TestElements.elements2();
    BytesData[] bytesData2 = bytesData1;
    MatrixDistancesSegments matrixDistancesSegments = new MatrixDistancesSegments(bytesData1, bytesData2);

    // When
    MatrixDistancesSegments result = matrixDistancesSegments.buildSegments();

    // Then
    Assertions.assertThat(result.segments).hasSize(3);

    MatrixDistancesSegment segment1 = result.segments.get(0);
    MatrixDistancesSegment segment2 = result.segments.get(1);
    Assertions.assertThat(segment1.element.id).isEqualTo("4");
    Assertions.assertThat(segment1.elements).hasSize(2);
    Assertions.assertThat(segment1.elements[0].id).isEqualTo("1");
    Assertions.assertThat(segment1.elements[1].id).isEqualTo("5");

    Assertions.assertThat(segment2.element.id).isEqualTo("1");
    Assertions.assertThat(segment2.elements).hasSize(1);
    Assertions.assertThat(segment2.elements[0].id).isEqualTo("5");
  }
}
