package com.orange.documentare.core.comp.clustering;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.stream.IntStream;

public class ClustersSizeTest {

  private static final int[] CLUSTERS_IDS = { 12, 14, 15, 34 };
  private static final int[] CLUSTERS_SIZE = { 5, 9, 100, 1 };

  @Test
  public void buildSizes() {
    // given
    DigitalTypes digitalTypes = buildDigitalTypes();
    // when
    ClustersSize clustersSize = new ClustersSize(digitalTypes);
    // then
    IntStream.range(0, CLUSTERS_IDS.length).forEach(
            i -> Assertions.
                    assertThat(clustersSize.sizeOf(CLUSTERS_IDS[i])).isEqualTo(CLUSTERS_SIZE[i])
    );
    Assertions.assertThat(clustersSize.clusterSizeOf(2)).isEqualTo(5);
    Assertions.assertThat(clustersSize.clusterSizeOf(6)).isEqualTo(9);
    Assertions.assertThat(clustersSize.clusterSizeOf(50)).isEqualTo(100);
    Assertions.assertThat(clustersSize.clusterSizeOf(114)).isEqualTo(1);
  }

  private DigitalTypes buildDigitalTypes() {
    DigitalTypes digitalTypes = new DigitalTypes();
    IntStream.range(0, CLUSTERS_IDS.length).forEach(
            i -> addDigitalTypes(digitalTypes, CLUSTERS_IDS[i], CLUSTERS_SIZE[i])
    );
    DigitalType space = new DigitalType();
    space.setSpace(true);
    digitalTypes.add(space);
    return digitalTypes;
  }

  private void addDigitalTypes(DigitalTypes digitalTypes, int clusterId, int count) {
    IntStream.range(0, count).forEach(
            i -> addNewDigitalType(digitalTypes, clusterId)
    );
  }

  private void addNewDigitalType(DigitalTypes digitalTypes, int clusterId) {
    DigitalType digitalType = new DigitalType();
    digitalType.setClusterId(clusterId);
    digitalTypes.add(digitalType);
  }
}
