package com.orange.documentare.core.model.ref.segmentation;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class SegmentationRect {

  @Getter
  @RequiredArgsConstructor
  public static class Size {
    final int width;
    final int height;
  }

  /** The absolute horizontal position of the zone in the page */
  @Getter(onMethod=@__({@JsonProperty("x")}))
  protected int x;

  /** The absolute vertical position of the zone in the page */
  @Getter(onMethod=@__({@JsonProperty("y")}))
  protected int y;

  /** The zone setWidth */
  @Getter(onMethod=@__({@JsonProperty("w")}))
  @JsonProperty("w")
  protected int width;

  /** The zone height */
  @Getter(onMethod=@__({@JsonProperty("h")}))
  @JsonProperty("h")
  protected int height;

  /**
   * @param x
   * @param y
   * @param width
   * @param height
   */
  @JsonIgnore
  public void setGeometry(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public void setGeometry(SegmentationRect r) {
    setGeometry(r.x, r.y, r.width, r.height);
  }

  @JsonIgnore
  public boolean isNotEmpty() {
    return width > 0 && height > 0;
  }

  public void union(SegmentationRect r) {
    if (r == null) {
      throw new IllegalArgumentException("provided rectangle should not be null");
    }
    int x1 = Math.min(x, r.x);
    int y1 = Math.min(y, r.y);
    int x2 = Math.max(x + width, r.x + r.width);
    int y2 = Math.max(y + height, r.y + r.height);
    x = x1;
    y = y1;
    width = x2 - x1;
    height =  y2 - y1;
  }

  public boolean isADiacriticOf(SegmentationRect r) {
    return x >= r.x && x < r.x + r.width;
  }

  public int xGapTo(SegmentationRect r) {
    return r.x - (x + width);
  }

  public boolean overlaps(SegmentationRect r) {
    return overlapsOnY(r) && overlapsOnX(r);
  }

  public boolean overlapsOnY(SegmentationRect r) {
    return yOverlap(r) > 0;
  }

  public int yOverlap(SegmentationRect r) {
    int top1 = y;
    int bottom1 = y + height;
    int top2 = r.y;
    int bottom2 = r.y + r.height;

    if (top1 < top2) top1 = top2;
    if (bottom1 > bottom2) bottom1 = bottom2;

    int overlappedHeight = bottom1 - top1;
    return overlappedHeight;
  }

  private boolean overlapsOnX(SegmentationRect r) {
    int left1 = x;
    int right1 = x + width;
    int left2 = r.x;
    int right2 = r.x + r.width;

    if (left1 < left2) left1 = left2;
    if (right1 > right2) right1 = right2;

    int overlappedWidth = right1 - left1;

    return overlappedWidth > 0;
  }

  public boolean isNoise(Size meanSize, float noiseRatio) {
    return width < meanSize.width/noiseRatio && height < meanSize.height/noiseRatio;
  }

  public int compareAreaTo(SegmentationRect r) {
    return Integer.compare(width * height, r.width * r.height);
  }

  public boolean equals(Object obj) {
    if(this == obj) {
      return true;
    } else if(!(obj instanceof SegmentationRect)) {
      return false;
    } else {
      SegmentationRect r = (SegmentationRect)obj;
      return x == r.x && y == r.y && width == r.width && height == r.height;
    }
  }

  public String toString() {
    return "{" + x + ", " + y + ", " + width + "x" + height + "}";
  }
}
