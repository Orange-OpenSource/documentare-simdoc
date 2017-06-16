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


import feign.Feign;
import feign.FeignException;
import feign.Response;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class RemoteClustering  {

  public void request(String url, ClusteringRequest clusteringRequest) {
    long t0 = System.currentTimeMillis();

    ResponseCollectorImpl.Clustering clustering  = buildFeignRequest(url);
    try {
      doRequest(url, clustering, clusteringRequest);
      long dt = (System.currentTimeMillis() - t0) / 1000;
//      log.info(String.format("[SUCCESS %d%s] from %s took %ds (%.2fs/elem)", progress(), '%', context.remoteService.url, dt, (float)dt/segment.get().elements.length));
    } catch (FeignException |IOException |InterruptedException e) {
      handleError(url, e);
    }
  }

  private void doRequest(String url, ResponseCollectorImpl.Clustering clustering, ClusteringRequest clusteringRequest) throws IOException, InterruptedException {
    log.info("[POST REQUEST] {}", url);
    RemoteTask remoteTask = clustering.clustering(clusteringRequest);
    ClusteringRequestResult result = waitForResult(remoteTask.id, url);
  }

  private void handleError(String url, Exception e) {
    if (e instanceof FeignException) {
      int status = ((FeignException) e).status();
      if (status == 503) {
        log.info("Service {} can not handle more tasks", url, status, e.getMessage());
      } else {
        log.error("Request to {} failed with status {}: {}", url, status, e.getMessage());
      }
    } else {
      log.error("Request to {} failed: {}", url, e.getMessage());
    }
  }
  private ResponseCollectorImpl.Clustering buildFeignRequest(String url) {
    return Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
           .target(ResponseCollectorImpl.Clustering.class, url);
  }

  private ClusteringRequestResult waitForResult(String taskId, String url) throws IOException, InterruptedException {
    WaitingPeriod waitingPeriod = new WaitingPeriod();
    ResponseCollectorImpl.ClusteringResult clusteringResult = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(ResponseCollectorImpl.ClusteringResult.class, url);
    Response response;
    do {
      waitingPeriod.sleep();
      response = clusteringResult.clusteringResult(taskId);
    } while (response.status() == 204);

    return (ClusteringRequestResult) (new JacksonDecoder())
            .decode(response, ClusteringRequestResult.class);
  }


}
