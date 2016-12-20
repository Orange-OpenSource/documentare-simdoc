package com.orange.documentare.core.comp.clustering.geometry;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Heron {
  /**
   * @param a
   * @param b
   * @param c
   * @return area thanks to Heron's formula
   */
  public static float get(float a, float b, float c) {
    float val =  (a+b+c) * (c-a+b) * (c+a-b) * (a+b-c);
    if (val <= 0) {
      String err = String.format("Invalid triangle detected during area computation: a = %f b = %f c = %f, val = %f", a, b, c, val);
      log.error(err);
      System.err.println(err);
      return 0;
    }
    return (float) (0.25f * Math.sqrt(val));
  }
}
