package com.orange.documentare.core.comp.distance;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.comp.NearestItem;
import org.fest.assertions.Assertions;
import org.junit.Test;

public class DistanceArrayNearestItemOfTest {


  @Test
  public void find_the_nearest_item_of_one_item() {
    // given
    DistancesArray distancesArray = new DistancesArray(1, 5, false);
    distancesArray.setDistancesForItem(0, new int[]{ 0, 40, 60, 20, 90 });
    // when
    NearestItem nearestItem = distancesArray.nearestItemOf(0);

    // then
    Assertions.assertThat(nearestItem.getIndex()).isEqualTo(3);
    Assertions.assertThat(nearestItem.getDistance()).isEqualTo(20);
  }

  @Test
  public void find_the_nearest_item_of_one_item_with_exclusion() {
    // given
    DistancesArray distancesArray = new DistancesArray(1, 5, false);
    distancesArray.setDistancesForItem(0, new int[]{ 0, 40, 60, 20, 90 });
    // when
    NearestItem nearestItem = distancesArray.nearestItemOfBut(0, 3);

    // then
    Assertions.assertThat(nearestItem.getIndex()).isEqualTo(1);
    Assertions.assertThat(nearestItem.getDistance()).isEqualTo(40);
  }
}
