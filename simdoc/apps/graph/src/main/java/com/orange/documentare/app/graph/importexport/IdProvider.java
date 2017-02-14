package com.orange.documentare.app.graph.importexport;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.jgrapht.dotexport.DOT;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import lombok.RequiredArgsConstructor;
import org.jgrapht.ext.VertexNameProvider;

@RequiredArgsConstructor
public class IdProvider implements VertexNameProvider<GraphItem> {

  @Override
  public String getVertexName(GraphItem graphItem) {
    return String.format("c%d_%s_%d", graphItem.getClusterId(), DOT.getWithoutAnyExtension(graphItem.getVertexName()), (int)(graphItem.getQ()*100));
  }
}
