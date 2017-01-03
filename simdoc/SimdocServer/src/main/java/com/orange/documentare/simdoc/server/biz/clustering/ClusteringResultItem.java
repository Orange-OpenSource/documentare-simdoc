package com.orange.documentare.simdoc.server.biz.clustering;

/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.system.filesid.FilesIdBuilder;
import com.orange.documentare.core.system.filesid.FilesIdMap;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.File;

@RequiredArgsConstructor
@EqualsAndHashCode
public class ClusteringResultItem {
  @ApiModelProperty(example = "/titi.pdf", required = true)
  public final String filename;
  @ApiModelProperty(example = "3", required = true)
  public final int clusterId;

  static ClusteringResultItem[] buildItems(File inputDirectory, File outputDirectory, SimClusteringItem[] simClusteringItems) {
    FilesIdBuilder filesIdBuilder = new FilesIdBuilder();
    FilesIdMap map = filesIdBuilder.readMapIn(outputDirectory.getAbsolutePath());
    ClusteringResultItem[] items = new ClusteringResultItem[simClusteringItems.length];

    for (int i = 0; i < items.length; i++) {
      int fileId = Integer.valueOf(simClusteringItems[i].getHumanReadableId());
      String fileAbsPath = map.get(fileId);
      String relPath = fileAbsPath.substring(inputDirectory.getAbsolutePath().length());
      items[i] = new ClusteringResultItem(relPath, simClusteringItems[i].getClusterId());
    }
    return items;
  }
}
