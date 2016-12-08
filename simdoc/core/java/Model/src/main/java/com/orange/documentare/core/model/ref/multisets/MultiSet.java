package com.orange.documentare.core.model.ref.multisets;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orange.documentare.core.model.ref.text.DigitalTypeIndices;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultiSet {

  public MultiSet(String clazz) {
    this.clazz = clazz;
  }

  /** class description, could be a letter for OCR mode */
  private String clazz;

  /** indices to digital types which are in this multiset */
  private DigitalTypeIndices digitalTypeIndices = new DigitalTypeIndices();

  /** digital types bytes arrays for this multiset, concatenated here */
  @Setter
  private byte[] dat;
}
