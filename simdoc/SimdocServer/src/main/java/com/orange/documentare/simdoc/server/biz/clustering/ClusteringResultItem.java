package com.orange.documentare.simdoc.server.biz.clustering;

import com.orange.documentare.core.system.filesid.FilesIdBuilder;
import com.orange.documentare.core.system.filesid.FilesIdMap;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class ClusteringResultItem {
  @ApiModelProperty(example = "user0/file/titi.pdf", required = true)
  public final String filename;
  @ApiModelProperty(example = "3", required = true)
  public final int clusterId;

  static ClusteringResultItem[] buildItems(FilesIdBuilder filesIdBuilder, ClusteringServiceImpl.SimClusteringItem[] simClusteringItems, String outputDirectoryAbsPath) {
    ClusteringResultItem[] items = new ClusteringResultItem[simClusteringItems.length];
    FilesIdMap map = filesIdBuilder.readMapIn(outputDirectoryAbsPath);
    for (int i = 0; i < items.length; i++) {
      int fileId = Integer.valueOf(simClusteringItems[i].getHumanReadableId());
      items[i] = new ClusteringResultItem(map.get(fileId), simClusteringItems[i].clusterId);
    }
    return items;
  }
}
