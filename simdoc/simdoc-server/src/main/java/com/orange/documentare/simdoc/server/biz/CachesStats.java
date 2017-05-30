package com.orange.documentare.simdoc.server.biz;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import com.orange.documentare.core.comp.ncd.Ncd;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CachesStats {
  public static void log() {
    log.info("Ncd C(x): " + Ncd.cacheStats().toString());
    log.info("BytesData file load: " + BytesData.cacheStats().toString());
  }
}
