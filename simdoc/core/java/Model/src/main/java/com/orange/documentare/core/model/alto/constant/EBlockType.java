
package com.orange.documentare.core.model.alto.constant;/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */


/**
 * The enumeration of the types of blocks in a page.
 *
 * @author ztrt8431
 */
public enum EBlockType {

  /**
   * The main page block
   */
  A,

  /**
   * The top margin in the page
   */
  N,

  /**
   * The right margin in the page
   */
  E,

  /**
   * The bottom margin in the page
   */
  B,

  /**
   * The left margin in the page
   */
  W,

  /**
   * The zone where the main text can be found in the page, aka PrintSpace
   */
  Z,

  /**
   * An illustration: picture, ...
   */
  I,

  /**
   * A graphical element: lettrine, ...
   */
  G,

  /**
   * A block composed of other blocks
   */
  C,

  /**
   * A paragraph
   */
  P,

  /**
   * A text line
   */
  L,

  /**
   * A separator between 2 words
   */
  S
}
