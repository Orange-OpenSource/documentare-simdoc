package com.orange.documentare.core.comp.ncd;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

/** Input for the NCD algorithm: contains data to be compressed and the data compressed length if already computed */
public class NcdInput {
  /** input data bytes */
  public final byte[] bytes;

  /** compressedLength result (length of the compressed data), if available */
  public final int compressedLength;

  public final boolean compressedLengthAvailable;

  /**
   * Build an instance with data but without an existing compressedLength result
   * @param bytes
   */
  public NcdInput(byte[] bytes) {
    this(bytes, 0, false);
  }

  /**
   * @param compressedLength
   * @return instance with data and data compressed length
   */
  public NcdInput withCompression(int compressedLength) {
    return new NcdInput(bytes, compressedLength, true);
  }

  protected NcdInput(byte[] bytes, int compressedLength, boolean compressedLengthAvailable) {
    this.bytes = bytes;
    this.compressedLength = compressedLength;
    this.compressedLengthAvailable = compressedLengthAvailable;
  }
}
