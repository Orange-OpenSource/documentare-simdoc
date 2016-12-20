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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/** A block in a page in the reference. */
@Accessors(fluent = true)
@Getter
@Setter
@NoArgsConstructor
public class Block extends SegmentationRect {
  @Getter(onMethod=@__({@JsonProperty("id")}))
  private int id;

  @Getter(onMethod=@__({@JsonProperty("t")}))
  private EBlockType type;

  public Block(int x, int y, int w, int h, EBlockType type) {
    super(x, y, w, h);
    this.type = type;
  }

  /** @return Whether the block is a line block */
  @JsonIgnore
  public boolean isLine() {
    return EBlockType.L == type();
  }

  /**
   * The enumeration of the types of blocks in a page.
   * Simplified version compared to portal version, since we only include elements required today
   */
  public enum EBlockType {
    /** The main page block */
    A,

    /** A paragraph */
    P,

    /** A text line */
    L
  }
}
