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
}
