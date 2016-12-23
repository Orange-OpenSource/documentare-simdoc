package com.orange.documentare.simdoc.server.biz.clustering;

import com.orange.documentare.core.system.filesid.FilesIdMap;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.File;

@RequiredArgsConstructor
@EqualsAndHashCode
public class ClusteringResultItem {
  @ApiModelProperty(example = "user0/file/titi.pdf", required = true)
  public final String filename;
  @ApiModelProperty(example = "3", required = true)
  public final int clusterId;

  static ClusteringResultItem[] buildItems(File inputDirectory, FilesIdMap map, ClusteringServiceImpl.SimClusteringItem[] simClusteringItems) {
    ClusteringResultItem[] items = new ClusteringResultItem[simClusteringItems.length];
    for (int i = 0; i < items.length; i++) {
      int fileId = Integer.valueOf(simClusteringItems[i].getHumanReadableId());
      String fileAbsPath = map.get(fileId);
      String relPath = fileAbsPath.substring(inputDirectory.getAbsolutePath().length());
      items[i] = new ClusteringResultItem(relPath, simClusteringItems[i].clusterId);
    }
    return items;
  }
}
