
package com.orange.documentare.core.model.alto.ref.page;/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public abstract class AZone {

  /** Block's id */
  private Integer id;

  /** The ID of the style that applies to the zone */
  private Integer sid;

  /** The absolute horizontal position of the zone in the page */
  private Integer x;

  /** The absolute vertical position of the zone in the page */
  private Integer y;

  /** The zone setWidth */
  private Integer w;

  /** The zone height */
  private Integer h;



  /**
   * Set zone's geometry
   * @param x
   * @param y
   * @param w setWidth
   * @param h height
   */
  public void setGeometry ( final int x, final int y, final int w, final int h ) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }


  /**
   * "Other" stuff, to be preserved during the deserialize / modify / serialize process
   */
  private final Map<String,Object> other = new HashMap<>();

  // "any getter" needed for serialization
  @JsonAnyGetter
  public Map<String,Object> any ( ) {
    return other;
  }

  @JsonAnySetter
  public void set ( final String name, final Object value ) {
    other.put(name, value);
  }


  /** @return the x */
  public Integer getX() {
    return x;
  }

  /** @return the y */
  public Integer getY() {
    return y;
  }

  /** @return the setWidth */
  public Integer getW() {
    return w;
  }

  /** @return the height */
  public Integer getH() {
    return h;
  }

  /** @return the id */
  public Integer getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(Integer id) {
    this.id = id;
  }

  /** @return the sid */
  public Integer getSid() {
    return sid;
  }

  /** @param sid the sid to set */
  public void setSid(Integer sid) {
    this.sid = sid;
  }
}
