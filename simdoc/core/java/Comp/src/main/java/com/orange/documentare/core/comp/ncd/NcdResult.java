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


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(suppressConstructorProperties = true)
public class NcdResult {
  public final float ncd;

  /**
   * compressed lengths are here for optimization purpose,
   * since NcdInput is immutable, we will use these results to
   * update data structures which may hold NcdInput for cache optimization
   */
  public final int input1CompressedLength;
  public final int input2CompressedLength;
}
