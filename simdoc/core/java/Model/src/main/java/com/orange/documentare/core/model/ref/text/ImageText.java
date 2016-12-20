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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import lombok.Getter;
import lombok.Setter;

/** Text model, ie mainly a vector of text elements, which can be indeed characters or operators (line feed, etc) */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageText {

  /** Ordered vector of text elements */
  private TextElements textElements;

  public static ImageText initFrom(DigitalTypes digitalTypes) {
    TextElements textElements = new TextElements();
    for (int i = 0; i < digitalTypes.size(); i++) {
      DigitalType digitalType = digitalTypes.get(i);
      TextElementType type = digitalType.isSpace() ? TextElementType.S : TextElementType.U;
      addTextElement(textElements, i, type, digitalType);
      checkLineFeed(textElements, i, digitalTypes);
    }

    ImageText imageText = new ImageText();
    imageText.setTextElements(textElements);
    return imageText;
  }

  private static void addTextElement(TextElements textElements, int index, TextElementType type, DigitalType digitalType) {
    DigitalTypeIndices digitalTypeIndices = new DigitalTypeIndices();
    digitalTypeIndices.add(index);
    TextElement textElement = new TextElement(type, digitalTypeIndices);
    updateDefaultDisplayString(textElement);
    textElements.add(textElement);
  }

  private static void checkLineFeed(TextElements textElements, int index, DigitalTypes digitalTypes) {
    if ((index < digitalTypes.size() - 1)) {
      if (digitalTypes.get(index).getLineId() != digitalTypes.get(index + 1).getLineId()) {
        TextElement textElement = new TextElement(TextElementType.L);
        updateDefaultDisplayString(textElement);
        textElements.add(textElement);
      }
    }
  }

  private static void updateDefaultDisplayString(TextElement textElement) {
    String c;
    switch (textElement.getType()) {
      case U: c = "*"; break;
      case S: c = " "; break;
      case L: c = "\n"; break;
      case H: c = "*"; break;
      default:
        throw new IllegalStateException("We should not have a CHAR type while initializing...");
    }
    textElement.setChars(c);
  }
}
