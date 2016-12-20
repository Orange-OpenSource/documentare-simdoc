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

import java.util.ArrayList;

class LyndonWordsRle extends ArrayList<WordRle> {

  // FIXME: really dirty, we need to rewrite this correctly

  void compute(LyndonWords words) {
    clear();
    int count = 1;
    for (int i = 0; i < words.size();) {
      if (words.get(i).isConstant()) {
        Word w = new Word();
        w.add(words.get(i).get(0));
        add(new WordRle(words.get(i).size(), w));
       i++;
      } else {
        for (int j = i + 1; j < words.size(); j++) {
          if (words.areEqual(i, j)) {
            count++;
          } else {
            break;
          }
        }
        add(new WordRle(count, words.get(i)));
        i += count;
        count = 1;
      }
    }
  }

  byte[] flattenToByteArray() {
    int len = flattenLen();
    byte[] bytes = new byte[len];
    int index = 0;
    for (WordRle wordRle : this) {
      byte[] bytesToCopy = wordRle.bytes();
      int copyLen = bytesToCopy.length;
      System.arraycopy(bytesToCopy, 0, bytes, index, copyLen);
      index += copyLen;
    }
    return bytes;
  }

  private int flattenLen() {
    int count = 0;
    for (WordRle wordRle : this) {
      count += wordRle.length();
    }
    return count;
  }
}
