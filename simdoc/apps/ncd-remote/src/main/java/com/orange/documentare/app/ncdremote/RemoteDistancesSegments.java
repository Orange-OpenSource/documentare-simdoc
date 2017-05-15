package com.orange.documentare.app.ncdremote;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.ncdremote.MatrixDistancesSegments.MatrixDistancesSegment;
import feign.Feign;
import feign.FeignException;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.IntStream;

@Slf4j
public class RemoteDistancesSegments implements RequestsProvider {
  private final List<MatrixDistancesSegment> segmentsToCompute;
  private final Map<Integer, MatrixDistancesSegment> idMap = new HashMap<>();

  private final ResponseCollector<MatrixDistancesSegment> responseCollector = new ResponseCollectorImpl();

  public RemoteDistancesSegments(MatrixDistancesSegments matrixDistancesSegments) {
    this.segmentsToCompute = new ArrayList<>(matrixDistancesSegments.segments);
    IntStream.range(0, segmentsToCompute.size())
            .forEach(index -> idMap.put(index, segmentsToCompute.get(index)));
  }

  public List<MatrixDistancesSegment> compute() {
    do {
      dispatchSegmentsToCompute();
    } while(!segmentsToCompute.isEmpty());

    return responseCollector.responses();
  }

  private void dispatchSegmentsToCompute() {
    RequestsProvider requestsProvider = this;
    LocalAvailableRemoteServices availableRemoteServices = new LocalAvailableRemoteServices();
    RequestsExecutor requestsExecutor = new RequestsExecutor(requestsProvider, responseCollector, availableRemoteServices);
    availableRemoteServices.setRequestExecutor(requestsExecutor);

    requestsExecutor.exec();
  }

  @Override
  public Optional<RequestExecutor> getPendingRequestExecutor() {
    return Optional.of(context -> {
      Optional<MatrixDistancesSegment> segment = nextSegment();
      if (!segment.isPresent()) {
        return;
      }

      ResponseCollectorImpl.Distance distanceRequest = Feign.builder()
              .encoder(new JacksonEncoder())
              .decoder(new JacksonDecoder())
              .target(ResponseCollectorImpl.Distance.class, context.remoteService.url);

      try {
        DistancesRequestResult result = distanceRequest.distance(segment.get());
        // FIXME: do not handle server error
        context.responseCollector.add(segment.get().withDistances(result.distances));
        log.info("[SUCCESS] from {}", context.remoteService.url);
      } catch (FeignException e) {
        log.error("Request to {} failed with status {}: {}", context.remoteService.url, e.status(),  e.getMessage());
        context.requestsProvider.failedToHandleRequest(getKeyByValue(idMap, segment.get()));
      }
    });
  }

  @Override
  public synchronized int pendingRequestsCount() {
    return segmentsToCompute.size();
  }

  private synchronized Optional<MatrixDistancesSegment> nextSegment() {
    if (segmentsToCompute.isEmpty()) {
      return Optional.empty();
    }

    MatrixDistancesSegment segment = segmentsToCompute.get(0);
    segmentsToCompute.remove(0);
    return Optional.of(segment);
  }

  @Override
  public synchronized boolean empty() {
    return segmentsToCompute.isEmpty();
  }

  @Override
  public synchronized void failedToHandleRequest(int requestId) {
    // re-add request in the pending list since it failed
    segmentsToCompute.add(idMap.get(requestId));
  }

  private int getKeyByValue(Map<Integer, MatrixDistancesSegment> map, MatrixDistancesSegment value) {
    return map.entrySet()
            .stream()
            .filter(entry -> Objects.equals(entry.getValue(), value))
            .map(Map.Entry::getKey)
            .findFirst()
            .get();
  }
}
