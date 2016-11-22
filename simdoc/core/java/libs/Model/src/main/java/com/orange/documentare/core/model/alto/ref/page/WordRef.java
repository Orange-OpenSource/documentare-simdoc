
package com.orange.documentare.core.model.alto.ref.page;/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */


import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class WordRef extends AZone {

  /**
   * Alto Word Confidence
   */
  private int aWc;

  /**
   * Whether the word can be found in a dictionary
   */
  private boolean aWd;

  /**
   * Words' characters, always present. Contains ALTO characters.
   * If segmentation succeeded, it contains segmentation results also.
   */
  private List <CharRef> cs = new ArrayList<>();

  /**
   * Words' characters, only present if segmentation failed, with segmentation results.
   */
  private List <CharRef> scs = new ArrayList<>();

  /**
   * Ordered list (hierarchy path) of blocks containing this word
   */
  private int [] bs;

  /**
   * Whether the segmentation succeeded
   * */
  private Boolean sg;

  /**
   * Word alto content, only needed to ease the work of the C++ part.
   * It should not be part of the final json.
   */
  private String c;
}
