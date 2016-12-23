package com.orange.documentare.simdoc.server.biz.clustering;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
@EqualsAndHashCode
public class ClusteringResult {
  @ApiModelProperty(example = "{ TODO }")
  public final ClusteringResultItem[] clustering;
  @ApiModelProperty(example = "true", required = true)
  public final boolean error;
  @ApiModelProperty(example = "inputDirectory is missing")
  public final String errorMessage;

  public static ClusteringResult error(String errorMessage) {
    return new ClusteringResult(null, true, errorMessage);
  }

  public static ClusteringResult with(ClusteringResultItem[] clusteringResultItems) {
    return new ClusteringResult(clusteringResultItems, false, null);
  }
}
