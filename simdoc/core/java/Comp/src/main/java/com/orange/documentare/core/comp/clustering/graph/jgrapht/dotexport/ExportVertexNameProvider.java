package com.orange.documentare.core.comp.clustering.graph.jgrapht.dotexport;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import org.jgrapht.ext.VertexNameProvider;

public class ExportVertexNameProvider implements VertexNameProvider<GraphItem> {
  @Override
  public String getVertexName(GraphItem graphItem) {
    return DOT.getDOTVertexName(graphItem.getVertexName());
  }
}
