package com.orange.documentare.simdoc.server.biz.clustering;

import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClusteringResult {
  @ApiModelProperty(example = "true", required = true)
  public final boolean error;
  @ApiModelProperty(example = "inputDirectory is missing")
  public final String errorMessage;

  public static ClusteringResult error(String errorMessage) {
    return new ClusteringResult(true, errorMessage);
  }
}
