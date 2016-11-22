
package com.orange.documentare.core.model.alto.ref.page;/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */


import com.orange.documentare.core.model.alto.constant.ECharType;

import java.util.Arrays;

/**
 * A character in a word in a page in the reference.
 */
public class CharRef extends AZone {

  /**
   * The character type
   */
  private ECharType t;

  /**
   * The character code
   */
  private String c;

  /**
   * The character confidence
   */
  private Integer ccf;

  /**
   * The characteristics vector
   */
  private double[] v;

  /**
   * The cluster ID to which this character belongs
   */
  private Long cl;

  /** The corresponding reference character ID */
  private Integer rfC;

  /** The corresponding user word ID */
  private Integer usC;



  /**
   * @return the t
   */
  public ECharType getT() {
    return t;
  }

  /**
   * @param t the t to set
   */
  public void setT(ECharType t) {
    this.t = t;
  }

  /**
   * @return the c
   */
  public String getC() {
    return c;
  }

  /**
   * @param c the c to set
   */
  public void setC(String c) {
    this.c = c;
  }

  /**
   * @return the ccf
   */
  public Integer getCcf() {
    return ccf;
  }

  /**
   * @param ccf the ccf to set
   */
  public void setCcf(Integer ccf) {
    this.ccf = ccf;
  }

  /**
   * @return the v
   */
  public double[] getV() {
    return v == null ? null : Arrays.copyOf(v, v.length);
  }

  /**
   * @param v the v to set
   */
  public void setV(double[] v) {
    this.v = v == null ? null : Arrays.copyOf(v, v.length);
  }

  /**
   * @return the cl
   */
  public Long getCl() {
    return cl;
  }

  /**
   * @param cl the cl to set
   */
  public void setCl(Long cl) {
    this.cl = cl;
  }

  /**
   * @return the rfC
   */
  public Integer getRfC() {
    return rfC;
  }

  /**
   * @param rfC the rfC to set
   */
  public void setRfC(Integer rfC) {
    this.rfC = rfC;
  }

  /**
   * @return the usC
   */
  public Integer getUsC() {
    return usC;
  }

  /**
   * @param usC the usC to set
   */
  public void setUsC(Integer usC) {
    this.usC = usC;
  }
}
