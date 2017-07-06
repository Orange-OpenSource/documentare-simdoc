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
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.system.inputfilesconverter.FilesMap;
import com.orange.documentare.simdoc.server.biz.FileIO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.IntStream;

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

  static ClusteringResultItem[] buildItemsInBytesDataModeWithFilesPreparation(BytesData[] bytesData, SimClusteringItem[] simClusteringItems, FileIO fileIO) {
    ClusteringResultItem[] items = new ClusteringResultItem[simClusteringItems.length];

    return fileIO.filesPrepped() ?
        buildItemsInBytesDataModeWithFilesPreparation(items, bytesData, simClusteringItems, fileIO) :
      buildItemsInBytesDataMode(items, bytesData, simClusteringItems);
  }

  private static ClusteringResultItem[] buildItemsInBytesDataModeWithFilesPreparation(ClusteringResultItem[] items, BytesData[] bytesData, SimClusteringItem[] simClusteringItems, FileIO fileIO) {
    FilesMap filesMap = fileIO.loadFilesMap();
    IntStream.range(0, items.length).forEach(i -> {
      BytesData bd = bytesData[i];
      int fileId = getKeyByValue(filesMap, bd.filepath).get();
      items[i] = new ClusteringResultItem(bd.id, lookUpSimClusteringItem(simClusteringItems, fileId).getClusterId());
    });
    return items;
  }

  private static ClusteringItem lookUpSimClusteringItem(SimClusteringItem[] simClusteringItems, int fileId) {
    return Arrays.stream(simClusteringItems)
      .filter(simClusteringItem -> Integer.parseInt(simClusteringItem.getHumanReadableId()) == fileId)
      .findFirst()
      .get();
  }

  private static ClusteringResultItem[] buildItemsInBytesDataMode(ClusteringResultItem[] items, BytesData[] bytesData, SimClusteringItem[] simClusteringItems) {
    for (int i = 0; i < items.length; i++) {
      items[i] = new ClusteringResultItem(bytesData[i].id, simClusteringItems[i].getClusterId());
    }
    return items;
  }

  public static Optional<Integer> getKeyByValue(Map<Integer, String> map, String value) {
    return map.entrySet()
      .stream()
      .filter(entry -> Objects.equals(entry.getValue(), value))
      .map(Map.Entry::getKey)
      .findFirst();
  }
}
