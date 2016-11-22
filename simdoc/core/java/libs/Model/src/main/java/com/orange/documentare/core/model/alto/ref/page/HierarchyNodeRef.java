/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

package com.orange.documentare.core.model.alto.ref.page;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * A node in the page blocks hierarchy of the reference.
 */
public class HierarchyNodeRef {

  /**
   * The identifier
   */
  @Getter
  @Setter
  private int id;

  /**
   * The list of the blocks contained in this block
   */
  @Getter
  @Setter
  private List<HierarchyNodeRef> ins = new ArrayList<>();


  /**
   * "Other" stuff, to be preserved during the deserialize / modify / serialize process
   */
  private Map<String,Object> other = new HashMap<>();

  // "any getter" needed for serialization
  @JsonAnyGetter
  public Map<String,Object> any ( ) {
      return other;
  }

  @JsonAnySetter
  public void set ( final String name, final Object value ) {
      other.put(name, value);
  }
}
