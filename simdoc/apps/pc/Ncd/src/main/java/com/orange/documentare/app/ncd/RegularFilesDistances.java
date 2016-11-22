package com.orange.documentare.app.ncd;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orange.documentare.core.comp.distance.DistancesArray;

@JsonInclude(JsonInclude.Include.NON_NULL)
class RegularFilesDistances {
  public final NcdItem[] items1;
  public final NcdItem[] items2;
  public final DistancesArray distancesArray;

  RegularFilesDistances(NcdItem[] items, DistancesArray distancesArray) {
    this(items, null, distancesArray);
  }

  RegularFilesDistances(NcdItem[] items1, NcdItem[] items2, DistancesArray distancesArray) {
    this.items1 = items1;
    this.items2 = items2;
    this.distancesArray = distancesArray;
  }
}
