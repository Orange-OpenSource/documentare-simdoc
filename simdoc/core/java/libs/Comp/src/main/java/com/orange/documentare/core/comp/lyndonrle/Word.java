package com.orange.documentare.core.comp.lyndonrle;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

class Word extends ArrayList<Byte> {

  @Setter
  @Getter
  private boolean constant = true;

  byte[] toByteArray() {
    byte[] bytes = new byte[size()];
    for (int i = 0; i < size(); i++) {
      bytes[i] = get(i);
    }
    return bytes;
  }
}
