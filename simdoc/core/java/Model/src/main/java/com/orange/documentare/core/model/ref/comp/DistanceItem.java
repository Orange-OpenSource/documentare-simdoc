package com.orange.documentare.core.model.ref.comp;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

public interface DistanceItem {
  /** Annoying but required for matrix print */
  String getHumanReadableId();
  /** data used for the distance computation */
  byte[] getBytes();
}
