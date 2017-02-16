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
import lombok.RequiredArgsConstructor;
import org.jgrapht.ext.ComponentAttributeProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor(suppressConstructorProperties = true)
public class VertexAttributeProvider implements ComponentAttributeProvider<GraphItem> {
  private static final String ALIEN_COLOR = "red";
  private static final String CENTER_COLOR = "green";
  private static final String REGULAR_COLOR = "black";
  private static final String FILLED_STYLE = "filled";

  private final Optional<String> imageDirectoryAbsPath;

  @Override
  public Map<String, String> getComponentAttributes(GraphItem graphItem) {
    Map<String, String> attrs = new HashMap<>();
    addColorAttr(attrs, graphItem);
    imageDirectoryAbsPath.ifPresent(path -> addImageAttr(attrs, graphItem));
    return attrs;
  }

  private void addImageAttr(Map<String, String> attrs, GraphItem graphItem) {
    String vertexImageFileName = String.format("%s/%s.png", imageDirectoryAbsPath.get(), graphItem.getVertexName());
    attrs.put("image", vertexImageFileName);
  }

  private void addColorAttr(Map<String, String> attrs, GraphItem graphItem) {
    String color = REGULAR_COLOR;
    String style = "";
    if (graphItem.isTriangleSingleton()) {
      color = ALIEN_COLOR;
      style = FILLED_STYLE;
    } else if (graphItem.isClusterCenter()) {
      color = CENTER_COLOR;
      style = FILLED_STYLE;
    }
    attrs.put("color", color);
    if (!style.isEmpty()) {
      attrs.put("style", style);
    }
  }
}
