package com.orange.documentare.core.image.segmentation;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.google.common.annotations.VisibleForTesting;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import com.orange.documentare.core.model.ref.segmentation.SegmentationRect;

import java.util.ArrayList;
import java.util.Arrays;

public class SpaceDetection {
  private static final float SPACE_RATIO = 2.6f;

  /** index where we will have to insert a space digital type */
  private class SpaceInsertionPositions extends ArrayList<Integer> {
  }

  /**
   * will insert detected 'space' digital types
   * @param digitalTypes
   */
  public void detect(DigitalTypes digitalTypes) {
    if (digitalTypes.size() > 2) {
      int meanDigitalTypesGap = getCentralMeanDigitalTypesGap(digitalTypes);
      SpaceInsertionPositions spaceInsertionPositions = detectSpacesWith(digitalTypes, meanDigitalTypesGap);
      insertSpaces(digitalTypes, spaceInsertionPositions);
    }
  }

  private SpaceInsertionPositions detectSpacesWith(DigitalTypes digitalTypes, int meanDigitalTypesGap) {
    SpaceInsertionPositions spaceInsertionPositions = new SpaceInsertionPositions();
    for (int i = 0; i < digitalTypes.size() - 1; i++) {
      DigitalType digitalTypeBeforeSpace = digitalTypes.get(i);
      if (isThereASpaceBetween(digitalTypeBeforeSpace, digitalTypes.get(i + 1), meanDigitalTypesGap)) {
        spaceInsertionPositions.add(i + 1);
      }
    }
    return spaceInsertionPositions;
  }

  private boolean isThereASpaceBetween(SegmentationRect left, SegmentationRect right, int meanDigitalTypesGap) {
    return left.xGapTo(right) > meanDigitalTypesGap * SPACE_RATIO;
  }

  private int getCentralMeanDigitalTypesGap(DigitalTypes digitalTypes) {
    int[] gaps = new int[digitalTypes.size()-1];
    for (int i = 0; i < digitalTypes.size() - 1; i++) {
      gaps[i] = digitalTypes.get(i).xGapTo(digitalTypes.get(i+1));
    }
    Arrays.sort(gaps);
    return getCentralMean(gaps);
  }

  @VisibleForTesting
  int getCentralMean(int[] gaps) {
    int lowIndex = (int) (gaps.length * 0.25f);
    int highIndex = (int) (gaps.length * 0.75f) - 1;
    if (lowIndex == highIndex + 1) {
      return gaps[lowIndex];
    } else {
      return getMeanOnRange(gaps, lowIndex, highIndex);
    }
  }

  private int getMeanOnRange(int[] gaps, int lowIndex, int highIndex) {
    int sum = 0;
    for (int i = lowIndex; i <= highIndex; i++) {
      sum += gaps[i];
    }
    return sum/(highIndex-lowIndex+1);
  }

  private void insertSpaces(DigitalTypes digitalTypes, SpaceInsertionPositions spaceInsertionPositions) {
    DigitalTypes copy = new DigitalTypes(digitalTypes);
    digitalTypes.clear();
    int lastInsertionPosition = 0;
    for (int i = 0; i < spaceInsertionPositions.size(); i++) {
      int spaceInsertionPosition = spaceInsertionPositions.get(i);
      digitalTypes.addAll(copy.subList(lastInsertionPosition, spaceInsertionPosition));
      DigitalType spaceType = copy.buildSpaceDigitalTypeBefore(spaceInsertionPosition);
      digitalTypes.add(spaceType);
      lastInsertionPosition = spaceInsertionPosition;
    }
    digitalTypes.addAll(copy.subList(lastInsertionPosition, copy.size()));
  }
}
