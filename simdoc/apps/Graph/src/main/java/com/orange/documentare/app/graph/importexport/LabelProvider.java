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

import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import com.orange.documentare.core.system.filesid.FilesIdMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jgrapht.ext.VertexNameProvider;

@RequiredArgsConstructor
public class LabelProvider implements VertexNameProvider<GraphItem> {
  private final FilesIdMap map;

  @Override
  public String getVertexName(GraphItem graphItem) {
    int index = Integer.parseInt(graphItem.getVertexName());
    String simpleFilename = map.simpleFilenameAt(index);
    return StringEscapeUtils.escapeHtml4(simpleFilename);
  }
}
