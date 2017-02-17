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

import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import com.orange.documentare.core.system.inputfilesconverter.FilesMap;
import com.orange.documentare.core.system.inputfilesconverter.InputFilesConverter;
import com.orange.documentare.simdoc.server.biz.FileIO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class ClusteringResultItem {
  @ApiModelProperty(example = "january/ticket-beijing.pdf", required = true)
  public final String id;
  @ApiModelProperty(example = "3", required = true)
  public final int clusterId;

  static ClusteringResultItem[] buildItemsInFilesMode(FileIO fileIO, SimClusteringItem[] simClusteringItems) {
    ClusteringResultItem[] items = new ClusteringResultItem[simClusteringItems.length];
    FilesMap map = fileIO.loadFilesMap();

    for (int i = 0; i < items.length; i++) {
      int fileId = Integer.valueOf(simClusteringItems[i].getHumanReadableId());
      String fileAbsPath = map.get(fileId);
      /* +1 to remove leading '/' */
      String relPath = fileAbsPath.substring(fileIO.inputDirectoryAbsPath.length() + 1);
      items[i] = new ClusteringResultItem(relPath, simClusteringItems[i].getClusterId());
    }
    return items;
  }

  static ClusteringResultItem[] buildItemsInBytesDataMode(BytesData[] bytesDatas, SimClusteringItem[] simClusteringItems) {
    ClusteringResultItem[] items = new ClusteringResultItem[simClusteringItems.length];
    for (int i = 0; i < items.length; i++) {
      items[i] = new ClusteringResultItem(bytesDatas[i].id, simClusteringItems[i].getClusterId());
    }
    return items;
  }
}
