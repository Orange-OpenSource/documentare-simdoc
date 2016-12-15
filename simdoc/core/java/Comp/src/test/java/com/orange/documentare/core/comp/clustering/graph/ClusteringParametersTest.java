package com.orange.documentare.core.comp.clustering.graph;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import org.fest.assertions.Assertions;
import org.junit.Test;

public class ClusteringParametersTest {

  @Test
  public void build_parameters_with_default_values() {
    // Given

    // When
    ClusteringParameters parameters = ClusteringParameters.builder().build();

    // Then
    Assertions.assertThat(parameters.qcut()).isFalse();
    Assertions.assertThat(parameters.acut()).isFalse();
    Assertions.assertThat(parameters.scut()).isFalse();
    Assertions.assertThat(parameters.ccut()).isFalse();
    Assertions.assertThat(parameters.knn()).isFalse();
  }

  @Test
  public void build_parameters() {
    // Given

    // When
    ClusteringParameters parameters = ClusteringParameters.builder()
            .acut()
            .qcut()
            .scut()
            .ccut()
            .wcut()
            .build();

    // Then
    Assertions.assertThat(parameters.qcut()).isTrue();
    Assertions.assertThat(parameters.acut()).isTrue();
    Assertions.assertThat(parameters.scut()).isTrue();
    Assertions.assertThat(parameters.ccut()).isTrue();
    Assertions.assertThat(parameters.knn()).isFalse();
  }


  @Test
  public void build_parameters_with_non_defaults() {
    // Given

    // When
    ClusteringParameters parameters = ClusteringParameters.builder()
            .acut(0.1f)
            .qcut(0.2f)
            .scut(0.3f)
            .ccut(23)
            .knn(12)
            .build();

    // Then
    Assertions.assertThat(parameters.acutSdFactor).isEqualTo(0.1f);
    Assertions.assertThat(parameters.qcutSdFactor).isEqualTo(0.2f);
    Assertions.assertThat(parameters.scutSdFactor).isEqualTo(0.3f);
    Assertions.assertThat(parameters.ccutPercentile).isEqualTo(23);
    Assertions.assertThat(parameters.kNearestNeighboursThreshold).isEqualTo(12);
  }
}
