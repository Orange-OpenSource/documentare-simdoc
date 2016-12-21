package com.orange.documentare.app.prepinputdir;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.distance.DistancesArray;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
class RegularFilesDistances {
  private NcdItem[] items1;
  private NcdItem[] items2;
  private DistancesArray distancesArray;

  void updateHumanReadableId() {
    Arrays.asList(items1).stream().forEach(item -> item.updateHumanReadableId());
    if (items2 != null) {
      Arrays.asList(items2).stream().forEach(item -> item.updateHumanReadableId());
    }
  }
}
