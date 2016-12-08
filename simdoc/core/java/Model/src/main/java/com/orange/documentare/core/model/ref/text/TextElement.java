package com.orange.documentare.core.model.ref.text;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextElement {

  private TextElementType type;

  /** this text element covers the following digital types */
  private DigitalTypeIndices digitalTypeIndices;

  /** the unicode text */
  private String chars;

  /** 'true' if the text was modified directly by the user, 'false' if it was propagated */
  private Boolean userModified;

  /** 'true' if chars does not match the propagation string of underlying Digital Type's cluster */
  private Boolean isolatedFromPropagation;

  public TextElement(TextElementType type) {
    this(type, null);
  }

  public TextElement(TextElementType type, DigitalTypeIndices digitalTypeIndices) {
    this.type = type;
    this.digitalTypeIndices = digitalTypeIndices;
  }

  @JsonIgnore
  public boolean userModified() {
    return userModified != null && userModified;
  }

  @JsonIgnore
  public boolean isAChar() {
    return type == TextElementType.C;
  }

  @JsonIgnore
  public boolean hasDigitalTypes() {
    return digitalTypeIndices != null;
  }
}
