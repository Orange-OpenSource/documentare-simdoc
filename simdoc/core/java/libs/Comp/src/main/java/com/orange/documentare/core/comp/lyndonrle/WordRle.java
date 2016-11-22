package com.orange.documentare.core.comp.lyndonrle;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

class WordRle {
  private final int count;
  private final Word word;

  WordRle(int count, Word word) {
    this.count = count;
    this.word = word;
  }

  int length() {
    return word.size() + 4;
  }

  byte[] bytes() {
    byte[] bytes = new byte[length()];
    bytes[3] = (byte)(count & 0xff);
    bytes[2] = (byte)(count>>8 & 0xff);
    bytes[1] = (byte)(count>>16 & 0xff);
    bytes[0] = (byte)(count>>24 & 0xff);
    byte[] bytesToCopy = word.toByteArray();
    System.arraycopy(bytesToCopy, 0, bytes, 4, bytesToCopy.length);
    return bytes;
  }
}
