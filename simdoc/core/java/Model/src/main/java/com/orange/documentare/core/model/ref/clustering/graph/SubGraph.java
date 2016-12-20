package com.orange.documentare.core.model.ref.clustering.graph;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class SubGraph extends GraphGroup {

  /** indices of contained clusters */
  private List<Integer> clusterIndices = new ArrayList<>();

  /** mean equilaterality for all contained items */
  private float meanQ;

  /** equilaterality's standard deviation for all contained items */
  private float standardDeviationQ;

  /** mean area for all contained items */
  private float meanArea;

  /** area's standard deviation for all contained items */
  private float standardDeviationArea;

  public SubGraph(int subGraphId, Collection<Integer> itemIndices, SummaryStatistics summaryStatisticsQ, SummaryStatistics summaryStatisticsArea) {
    minInit(subGraphId, itemIndices, (float) summaryStatisticsQ.getMean());
    standardDeviationQ = (float) summaryStatisticsQ.getStandardDeviation();
    meanArea = (float) summaryStatisticsArea.getMean();
    standardDeviationArea = (float) summaryStatisticsArea.getStandardDeviation();
    debug();
  }

  public SubGraph(int subGraphId, Collection<Integer> itemIndices, float meanQ) {
    minInit(subGraphId, itemIndices, meanQ);
  }

  private void minInit(int subGraphId, Collection<Integer> itemIndices, float meanQ) {
    setGroupId(subGraphId);
    getItemIndices().addAll(itemIndices);
    this.meanQ = meanQ;
  }

  private void debug() {
    if (log.isDebugEnabled()) {
      StringBuilder stringBuilderIndices = new StringBuilder();
      for (Integer index : getItemIndices()) {
        stringBuilderIndices.append(String.format("%d ", index));
      }
      log.debug("Subgraph {} [{}] [{} {}] [{} {}]", getGroupId(), stringBuilderIndices.toString(), meanQ, standardDeviationQ, meanArea, standardDeviationArea);
    }
  }
}
