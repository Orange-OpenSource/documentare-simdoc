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

public class Equilaterality {

  /**
   * @param area
   * @param edge12
   * @param edge23
   * @param edge31
   * @return equilaterality factor
   */
  public static float get(float area, int edge12, int edge23, int edge31) {
    float meanEdge = ((float)edge12 + edge23 + edge31) / 3;
    float q = area / (meanEdge*meanEdge);
    q = (float) ((q * 4) / Math.sqrt(3));
    return q;
  }
}
