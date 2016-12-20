package com.orange.documentare.core.image.transformations;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.image.connectedcomponents.ConnectedComponent;
import com.orange.documentare.core.image.glyphs.Glyph;
import com.orange.documentare.core.image.glyphs.Glyphs;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import org.fest.assertions.Assertions;
import org.junit.Test;

public class GlyphsToDigitalTypesTransformationTest {

  @Test
  public void buildDigitalTypes() {
    // given
    int x1 = 1;
    int y1 = 2;
    int w1 = 4;
    int h1 = 4;
    int x2 = 10;
    int y2 = 3;
    int w2 = 5;
    int h2 = 5;
    int lineY = 1;
    int lineHeight = 20;
    ConnectedComponent connectedComponent1 = new ConnectedComponent(x1, y1, w1, h1);
    ConnectedComponent connectedComponent2 = new ConnectedComponent(x2, y2, w2, h2);
    Glyphs glyphs = new Glyphs();
    glyphs.add(new Glyph(connectedComponent1));
    glyphs.add(new Glyph(connectedComponent2));
    GlyphsToDigitalTypes glyphsToDigitalTypes = new GlyphsToDigitalTypes();
    DigitalType expectedDigitalType1 = new DigitalType(x1, lineY, w1, lineHeight);
    DigitalType expectedDigitalType2 = new DigitalType(x2, lineY, w2, lineHeight);

    // when
    DigitalTypes digitalTypes = glyphsToDigitalTypes.transform(glyphs, lineY, lineHeight);

    // then
    Assertions.assertThat(digitalTypes).hasSize(2);
    Assertions.assertThat(digitalTypes.get(0)).isEqualTo(expectedDigitalType1);
    Assertions.assertThat(digitalTypes.get(1)).isEqualTo(expectedDigitalType2);
  }
}
