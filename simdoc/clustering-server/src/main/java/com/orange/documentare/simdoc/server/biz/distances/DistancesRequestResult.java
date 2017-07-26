package com.orange.documentare.simdoc.server.biz.distances;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
@EqualsAndHashCode
public class DistancesRequestResult {
  @ApiModelProperty(example = "{ distances: [ 0, 234000, 454546, ... ] }")
  public final int[] distances;
  @ApiModelProperty(example = "false", required = true)
  public final boolean error;
  @ApiModelProperty()
  public final String errorMessage;

  public static DistancesRequestResult error(String errorMessage) {
    return new DistancesRequestResult(null, true, errorMessage);
  }

  public static DistancesRequestResult with(int[] distances) {
    return new DistancesRequestResult(distances,false, null);
  }
}
