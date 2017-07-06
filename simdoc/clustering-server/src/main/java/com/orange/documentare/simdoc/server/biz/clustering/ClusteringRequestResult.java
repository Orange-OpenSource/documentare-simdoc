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

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
@EqualsAndHashCode
public class ClusteringRequestResult {
  @ApiModelProperty(example = "{ 'clustering': [ {'id': 'january/ticket-beijing.pdf', 'clusterId': 1}, ... ], 'error': false }")
  public final ClusteringResultItem[] clustering;
  @ApiModelProperty(example = "false", required = true)
  public final boolean error;
  @ApiModelProperty()
  public final String errorMessage;

  public static ClusteringRequestResult error(String errorMessage) {
    return new ClusteringRequestResult(null, true, errorMessage);
  }

  public static ClusteringRequestResult with(ClusteringResultItem[] clusteringResultItems) {
    return new ClusteringRequestResult(clusteringResultItems, false, null);
  }
}
