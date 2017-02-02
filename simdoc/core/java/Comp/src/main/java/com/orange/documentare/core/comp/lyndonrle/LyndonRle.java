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

import com.orange.documentare.core.comp.ncd.CompressedLengthMethod;

// FIXME: remove?
@Deprecated
public class LyndonRle implements CompressedLengthMethod {

  @Override
  public int computeCompressedLengthOf(byte[] inputArray) {
    return compress(inputArray).length;
  }

  public byte[] compress(byte[] bytes) {
    LyndonWords lyndonWords = findLyndonWords(bytes);
    LyndonWordsRle lyndonWordsRle = lyndonWords.rle();
    return lyndonWordsRle.flattenToByteArray();
  }

  private class State {
    final byte[] bytes;
    final LyndonWords lyndonWords = new LyndonWords();

    int nextIndex;
    boolean finished;

    public State(byte[] bytes) {
      this.bytes = bytes;
    }
  }

  private LyndonWords findLyndonWords(byte[] bytes) {
    State state = new State(bytes);
    do {
      Word word = findNextWord(state);
      state.lyndonWords.add(word);
    } while(!state.finished);

    return state.lyndonWords;
  }

  private Word findNextWord(State state) {
    byte[] b = state.bytes;
    int len = b.length;
    Word w = new Word();
    w.add(b[state.nextIndex]);
    for (int i = state.nextIndex + 1; i < len; i++) {
      if (b[i - 1] == b[i]) {
        if (w.isConstant()) {
          w.add(b[i]);
        } else {
          break;
        }
      } else if (b[i - 1] > b[i]) {
        break;
      } else if (w.size() == 1) {
        w.add(b[i]);
        w.setConstant(false);
      } else if (!w.isConstant()) {
        w.add(b[i]);
      } else {
        break;
      }
    }
    state.nextIndex += w.size();
    state.finished = state.nextIndex >= len;
    return w;
  }
}
