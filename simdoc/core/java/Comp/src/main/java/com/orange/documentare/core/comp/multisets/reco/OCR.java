package com.orange.documentare.core.comp.multisets.reco;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.ncd.Ncd;
import com.orange.documentare.core.comp.ncd.NcdInput;
import com.orange.documentare.core.comp.ncd.NcdResult;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import com.orange.documentare.core.model.ref.multisets.DigitalTypeClass;
import com.orange.documentare.core.model.ref.multisets.DigitalTypesClasses;
import com.orange.documentare.core.model.ref.multisets.MultiSet;
import com.orange.documentare.core.model.ref.multisets.MultiSets;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import com.orange.documentare.core.model.ref.text.ImageText;
import com.orange.documentare.core.model.ref.text.TextElementType;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OCR {

  class OcrNcdInput extends NcdInput {
    private final String clazz;

    public OcrNcdInput(byte[] bytes, String clazz) {
      this(clazz, bytes, 0, false);
    }

    private OcrNcdInput(String clazz, byte[] bytes, int compressedLength, boolean compressedLengthAvailable) {
      super(bytes, compressedLength, compressedLengthAvailable);
      this.clazz = clazz;
    }

    @Override
    public OcrNcdInput withCompression(int compressedLength) {
      return new OcrNcdInput(clazz, bytes, compressedLength, true);
    }
  }

  @RequiredArgsConstructor
  private static class OcrNcdResult {
    private final float ncd;
    private final DigitalTypeClass digitalTypeClass;

    static int compare(OcrNcdResult o1, OcrNcdResult o2) {
      return Float.compare(o1.ncd, o2.ncd);
    }
  }

  private final Ncd ncd = new Ncd();

  final List<OcrNcdInput> multiSetNcdInputs;

  public OCR(MultiSets multiSets) {
    multiSetNcdInputs = multiSets.stream()
            .map(multiSet -> getOcrNcdInputFor(multiSet))
            .collect(Collectors.toList());
  }

  public DigitalTypesClasses doRecoOn(DigitalTypes digitalTypes) {
    return digitalTypes.stream()
            .map(digitalType -> computeClassFor(digitalType))
            .collect(Collectors.toCollection(DigitalTypesClasses::new));
  }

  public ImageText buildImageText(DigitalTypes digitalTypes, DigitalTypesClasses digitalTypeClasses) {
    ImageText imageText = ImageText.initFrom(digitalTypes);
    imageText.getTextElements().stream()
            .filter(el -> el.getType() == TextElementType.U)
            .forEach( textElement -> {
      int index = textElement.getDigitalTypeIndices().get(0);
      textElement.setChars(digitalTypeClasses.get(index).getClazz());
      textElement.setType(TextElementType.C);
    });
    return imageText;
  }

  private OcrNcdInput getOcrNcdInputFor(MultiSet multiSet) {
    return new OcrNcdInput(multiSet.getDat(), multiSet.getClazz());
  }

  private DigitalTypeClass computeClassFor(DigitalType digitalType) {
    if (digitalType.isSpace()) {
      return new DigitalTypeClass(" ", 0);
    }
    NcdInput digitalTypeNcdInput = new NcdInput(digitalType.getBytes());
    List<OcrNcdInput> multiSetClone = new ArrayList<>(multiSetNcdInputs);
    return multiSetClone.stream()
            .map(multisetNcdInput -> getNcdDistanceFor(multisetNcdInput, digitalTypeNcdInput))
            .min(OcrNcdResult::compare)
            .get()
            .digitalTypeClass;
  }

  private OcrNcdResult getNcdDistanceFor(OcrNcdInput multisetNcdInput, NcdInput digitalTypeNcdInput) {
    NcdResult result = ncd.computeNcd(multisetNcdInput, digitalTypeNcdInput);
    updateMultisetCache(multisetNcdInput, result.input1CompressedLength);
    return new OcrNcdResult(result.ncd, new DigitalTypeClass(multisetNcdInput.clazz, result.ncd));
  }

  private synchronized void updateMultisetCache(OcrNcdInput multisetNcdInput, int compressedLength) {
    multiSetNcdInputs.remove(multisetNcdInput);
    multiSetNcdInputs.add(multisetNcdInput.withCompression(compressedLength));
  }
}
