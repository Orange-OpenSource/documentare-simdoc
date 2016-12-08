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

class LyndonWords extends ArrayList<Word> {

  LyndonWordsRle rle() {
    LyndonWordsRle lyndonWordsRle = new LyndonWordsRle();
    lyndonWordsRle.compute(this);
    return lyndonWordsRle;
  }

  boolean areEqual(int i, int j) {
    return get(i).equals(get(j));
  }
}
