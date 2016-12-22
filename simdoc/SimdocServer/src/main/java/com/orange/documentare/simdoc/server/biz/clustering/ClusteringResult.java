package com.orange.documentare.simdoc.server.biz.clustering;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClusteringResult {
  public final boolean error;
  public final String errorMessage;

  public static ClusteringResult error(String errorMessage) {
    return new ClusteringResult(true, errorMessage);
  }
}
