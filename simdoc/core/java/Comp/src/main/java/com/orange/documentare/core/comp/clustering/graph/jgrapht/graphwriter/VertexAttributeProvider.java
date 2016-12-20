package com.orange.documentare.core.comp.clustering.graph.jgrapht.graphwriter;
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

@RequiredArgsConstructor(suppressConstructorProperties = true)
public class VertexAttributeProvider implements ComponentAttributeProvider<GraphItem> {
  private static final String ALIEN_COLOR = "red";
  private static final String CENTER_COLOR = "green";
  private static final String REGULAR_COLOR = "black";
  private static final String FILLED_STYLE = "filled";

  private final String imageDirectory;

  @Override
  public Map<String, String> getComponentAttributes(GraphItem graphItem) {
    Map<String, String> attrs = new HashMap<>();
    addColorAttr(attrs, graphItem);
    addImageAttr(attrs, graphItem);
    return attrs;
  }

  private void addImageAttr(Map<String, String> attrs, GraphItem graphItem) {
    String verticeFileName = String.format("%s/%s.png", imageDirectory, graphItem.getVertexName());
    attrs.put("image", verticeFileName);
  }

  private boolean isImage(String fileName) {
    String fileNameLowerCase = fileName.toLowerCase();
    return fileNameLowerCase.endsWith(".png")   ||
           fileNameLowerCase.endsWith(".bmp")   ||
           fileNameLowerCase.endsWith(".jpg")   ||
           fileNameLowerCase.endsWith(".jpeg")  ||
           fileNameLowerCase.endsWith(".tiff")  ||
           fileNameLowerCase.endsWith(".tif");
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
