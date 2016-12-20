package com.orange.documentare.core.comp.ncd;
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
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(suppressConstructorProperties = true)
/** Input for the NCD algo, contains data to be compressed, plus the compression result of already computed */
public class NcdInput {
  private final byte[] bytes;

  private int compLength;
  private boolean compLengthAvailable;

  public synchronized void setCompLength(int length) {
    compLength = length;
    compLengthAvailable = true;
  }
}
