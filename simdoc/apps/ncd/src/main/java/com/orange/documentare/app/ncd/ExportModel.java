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
import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExportModel {
  @RequiredArgsConstructor
  public class Item {
    public final String relativeFilename;
  }

  public final Item[] items1;
  public final Item[] items2;
  public final DistancesArray distancesArray;

  public ExportModel(BytesData[] bytesData1, BytesData[] bytesData2, DistancesArray distancesArray) {
    this.items1 = buildItems(bytesData1);
    this.items2 = bytesData1 == bytesData2 ? null : buildItems(bytesData2);
    this.distancesArray = distancesArray;
  }

  private Item[] buildItems(BytesData[] bytesDataArray) {
    return Arrays.stream(bytesDataArray)
      .map(bytesData -> bytesData.id)
      .map(Item::new)
      .toArray(size -> new Item[size]);
  }
}
