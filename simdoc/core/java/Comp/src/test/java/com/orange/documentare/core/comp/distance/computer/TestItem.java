package com.orange.documentare.core.comp.distance.computer;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.comp.DistanceItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
class TestItem implements DistanceItem {
  private final String fileName;
  private final byte[] bytes;
  private final int itemId;

  @Override
  public String getHumanReadableId() {
    return fileName;
  }

  @Override
  public boolean equals(Object obj) {
    TestItem otherItem = (TestItem) obj;
    return fileName.equals(otherItem.getFileName());
  }
}
