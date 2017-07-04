package com.orange.documentare.app.clusteringremote;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */


import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ResponseCollectorImpl  implements ResponseCollector<ClusteringRequest>{

  @Override
  public void add(ClusteringRequest clusteringRequest) {
  }

  @Override
  public List<ClusteringRequest> responses() {
    return null;
  }

  @Override
  public boolean allResponsesCollected() {
    return false;
  }

  interface Clustering {
    @RequestLine("POST /clustering")
    @Headers("Content-Type: application/json")
    RemoteTask clustering(ClusteringRequest clusteringRequest);
  }

  interface ClusteringResult {
    @RequestLine("GET /task/{taskId}")
    Response clusteringResult(@Param("taskId") String taskId);
  }
}
