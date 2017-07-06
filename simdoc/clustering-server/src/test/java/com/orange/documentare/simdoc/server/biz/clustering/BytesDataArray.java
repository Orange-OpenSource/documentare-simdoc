package com.orange.documentare.simdoc.server.biz.clustering;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Denis Boisset & Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BytesDataArray {
  public final BytesData[] bytesData;
}
