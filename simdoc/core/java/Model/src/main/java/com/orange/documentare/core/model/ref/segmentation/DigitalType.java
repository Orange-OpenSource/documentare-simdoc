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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.comp.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Image Digital type:
 *  - aggregates connected components to build a glyph (includes diacritics, eyes, etc), ie. the image of a character
 *  - it aims to match the Metal type since we stretch the original image glyph to match the line height
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DigitalType extends SegmentationRect implements DistanceItem, ClusteringItem {

  public DigitalType(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  /** Parent block id containing the glyph */
  private Integer lineId;

  /** indicates if this type is suspected to be a 'space' */
  private Boolean space;

  /** The cluster ID to which this character belongs */
  private Integer clusterId;

  /** The cluster ID to which this character belongs */
  private Boolean clusterCenter;

  /** For clustering computation; Given that glyphs are ordered in an array,
   * here we have the "nearests" (from a distance point of view) elements indices in this array */
  private NearestItem[] nearestItems;

  /** For optimization purpose in triangulation, we can only keep triangulation info */
  private TriangleVertices triangleVertices;

  /** Binary cropped image of the glyph. For distance computation; Please note that jackson handles Base64 conversion for us! */
  private byte[] bytes;

  @Override
  public void setClusterCenter(boolean isCenter) {
    this.clusterCenter = isCenter ? true : null;
  }

  public boolean isClusterCenter() {
    return clusterCenter != null && clusterCenter;
  }


  /** Useful for debugEnabled purpose in distance or clustering algorithms */
  @JsonIgnore
  public String getHumanReadableId() {
    return y + "_" + x;
  }

  public boolean isSpace() {
    return space != null && space;
  }

  @Override
  public boolean triangleVerticesAvailable() {
    return triangleVertices != null;
  }

  @Override
  public String toString() {
    return super.toString() + (isSpace() ? " / space = true" : "") + (clusterId == null ? "" : " / cluster id = " + clusterId);
  }

  /** Remove distances and image bytes */
  public void strip() {
    nearestItems = null;
    bytes = null;
  }
}
