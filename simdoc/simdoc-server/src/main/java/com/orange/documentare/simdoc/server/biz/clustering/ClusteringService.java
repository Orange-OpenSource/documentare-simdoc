package com.orange.documentare.simdoc.server.biz.clustering;

/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.simdoc.server.biz.FileIO;

import java.io.File;
import java.io.IOException;

interface ClusteringService {
  ClusteringRequestResult build(FileIO fileIO, ClusteringParameters parameters, boolean debug) throws IOException;
}
