package com.orange.documentare.core.image.linedetection;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orange.documentare.core.image.connectedcomponents.ConnectedComponent;
import com.orange.documentare.core.image.connectedcomponents.ConnectedComponents;
import com.orange.documentare.core.image.segmentationcollection.OuterRectBuilder;
import com.orange.documentare.core.model.ref.segmentation.SegmentationRect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Accessors(fluent = true)
public class Line extends SegmentationRect {
  @Getter(onMethod=@__({@JsonProperty("connectedComponents")}))
  @Setter
  private ConnectedComponents connectedComponents = new ConnectedComponents();

  private final OuterRectBuilder outerRectBuilder = new OuterRectBuilder();

  public Line() {
    reset();
  }

  private void reset() {
    connectedComponents.clear();
    outerRectBuilder.reset();
  }

  public boolean couldContain(SegmentationRect r, int lineMaxHeight) {
    if (connectedComponents.isEmpty()) {
      return true;
    } else {
      return outerRectBuilder.isAboveYMax(r) && isNotExpandingHeightTooFar(r, lineMaxHeight);
    }
  }

  private boolean isNotExpandingHeightTooFar(SegmentationRect r, int lineMaxHeight) {
    return r.y() + r.height() - y <= lineMaxHeight;
  }

  void refreshSize() {
    for (ConnectedComponent connectedComponent : connectedComponents) {
      refreshSizeFor(connectedComponent);
    }
  }

  void add(ConnectedComponent connectedComponent) {
    connectedComponents.add(connectedComponent);
    refreshSizeFor(connectedComponent);
  }

  private void refreshSizeFor(SegmentationRect rect) {
    outerRectBuilder.add(rect);
    setGeometry(outerRectBuilder.xMin(), outerRectBuilder.yMin(), outerRectBuilder.width(), outerRectBuilder.height());
  }

  void addButNotGrow(ConnectedComponent connectedComponent) {
    connectedComponents.add(connectedComponent);
  }

  public Lines findSubColumns(boolean alienDetection) {
    SubColumns subColumns = new SubColumns(this, alienDetection);
    return subColumns.findSubColumns();
  }

  public Lines findSubLines() {
    SubLines subLines = new SubLines(this);
    return subLines.findSubLines();
  }

  public float elemMaxHeight() {
    return outerRectBuilder.elemMaxHeight();
  }
}
