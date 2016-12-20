package com.orange.documentare.core.comp.multisets;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.multisets.MultiSet;
import com.orange.documentare.core.model.ref.multisets.MultiSets;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import com.orange.documentare.core.model.ref.text.DigitalTypeIndices;
import com.orange.documentare.core.model.ref.text.ImageText;
import com.orange.documentare.core.model.ref.text.TextElement;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Build a multiset starting from Text elements and associated digital
 * types indices. We can add digital types bytes by providing the segmentation model.
 */
public class MultiSetsBuilder {

  public static final boolean INCLUDE_PROPAGATED_CHARS_DEFAULT = true;
  public static final int CLASS_SIZE_MIN_DEFAULT = 2;

  /** whether it is allowed to include propagated chars in a multiset or not */
  private final boolean includePropagatedChars;

  /** a semantic class (characters) which has less elements than this value can not be kept as a multiset */
  private final int classSizeMin;

  /**
   * @param includePropagatedChars true to allow including propagating chars in multisets
   * @param classSizeMin a semantic class (characters) which has less elements than this value can not be kept as a multiset
   */
  public MultiSetsBuilder(boolean includePropagatedChars, int classSizeMin) {
    this.includePropagatedChars = includePropagatedChars;
    this.classSizeMin = classSizeMin;
  }

  public MultiSetsBuilder() {
    this(INCLUDE_PROPAGATED_CHARS_DEFAULT, CLASS_SIZE_MIN_DEFAULT);
  }

  /**
   * Build multisets, with the digital types indices
   * @param imageText
   * @return multisets with only digital types indices (bytes array is not available)
   */
  public MultiSets build(ImageText imageText) {
    Map<String, MultiSet> multiSetMap = buildClassToMultisetMap(imageText);
    Collection<MultiSet> multisets = extractMultisets(multiSetMap);
    return new MultiSets(multisets);
  }

  /**
   * Limit class size, using clustering center info and random selection for the rest
   * @param multiSets
   * @param imageSegmentation
   * @param maxSize
   */
  public void limitClassSize(MultiSets multiSets, ImageSegmentation imageSegmentation, int maxSize) {
    multiSets.stream()
            .filter(multiset -> multiset.getDigitalTypeIndices().size() > maxSize)
            .forEach(multiset -> limitNbOfElementsPerClass(multiset, imageSegmentation.getDigitalTypes(), maxSize));
  }

  /**
   * Given multisets with digital types indices info, update the bytes arrays
   * @param multiSets multisets for which we want to update the bytes array
   * @param imageSegmentation contains digital types bytes arrays
   */
  public void addBytes(MultiSets multiSets, ImageSegmentation imageSegmentation) {
    DigitalTypes digitalTypes = imageSegmentation.getDigitalTypes();
    multiSets.stream().forEach(multiSet -> addBytes(multiSet, digitalTypes));
  }

  private Map<String, MultiSet> buildClassToMultisetMap(ImageText imageText) {
    Map<String, MultiSet> multiSetMap = new HashMap<>();
    imageText.getTextElements().stream()
            .filter(textElement -> isTextClassValid(textElement))
            .forEach(textElement -> addToMultiset(textElement, multiSetMap)
            );
    return multiSetMap;
  }

  private Collection<MultiSet> extractMultisets(Map<String, MultiSet> multiSetMap) {
    return multiSetMap.values().stream()
            .filter(multiSet -> multiSet.getDigitalTypeIndices().size() >= classSizeMin)
            .collect(Collectors.toList());
  }

  private boolean isTextClassValid(TextElement el) {
    return el.isAChar() && (el.userModified() || includePropagatedChars);
  }

  private void addToMultiset(TextElement el, Map<String, MultiSet> multiSetMap) {
    String clazz = el.getChars();
    MultiSet multiSet = multiSetMap.computeIfAbsent(clazz, key -> new MultiSet(key));
    multiSet.getDigitalTypeIndices().addAll(el.getDigitalTypeIndices());
  }

  private void limitNbOfElementsPerClass(MultiSet multiset, DigitalTypes digitalTypes, int maxSize) {
    DigitalTypeIndices digitalTypeIndices = multiset.getDigitalTypeIndices();
    List<Integer> centers = digitalTypeIndices.stream()
            .filter(index -> digitalTypes.get(index).isClusterCenter())
            .collect(Collectors.toList());

    if (centers.size() >= maxSize) {
      digitalTypeIndices.clear();
      Collections.shuffle(centers);
      digitalTypeIndices.addAll(centers.subList(0, maxSize));
    } else {
      digitalTypeIndices.removeAll(centers);
      Collections.shuffle(digitalTypeIndices);
      digitalTypeIndices.retainAll(digitalTypeIndices.subList(0, maxSize - centers.size()));
      digitalTypeIndices.addAll(centers);
    }
  }

  private void addBytes(MultiSet multiSet, DigitalTypes digitalTypes) {
    BytesHolder bytesHolder = new BytesHolder();
    multiSet.getDigitalTypeIndices().forEach(
            index -> bytesHolder.add(digitalTypes.get(index).getBytes())
    );
    multiSet.setDat(bytesHolder.flatten());
  }
}
