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
import feign.*;
import feign.Response;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

@Slf4j
public class RemoteDistancesSegments implements RequestsProvider {
  private final Map<Integer, MatrixDistancesSegment> idMap = new HashMap<>();
  private final List<MatrixDistancesSegment> segmentsToCompute;
  private final ResponseCollector<MatrixDistancesSegment> responseCollector;

  public RemoteDistancesSegments(MatrixDistancesSegments matrixDistancesSegments) {
    this.segmentsToCompute = new ArrayList<>(matrixDistancesSegments.segments);
    responseCollector = new ResponseCollectorImpl(segmentsToCompute.size());
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
    requestsExecutor.exec();
  }

  @Override
  public Optional<RequestExecutor> getPendingRequestExecutor() {
    return Optional.of(context -> {
      Optional<MatrixDistancesSegment> segment = nextSegment();
      if (!segment.isPresent()) {
        return;
      }
      request(context, segment);
    });
  }

  private void request(ExecutorContext context, Optional<MatrixDistancesSegment> segment) {
    long t0 = System.currentTimeMillis();

    ResponseCollectorImpl.Distance distanceRequest = buildFeignRequest(context);
    try {
      doRequest(context, segment, distanceRequest);
      long dt = (System.currentTimeMillis() - t0) / 1000;
      log.info(String.format("[SUCCESS %d%s] from %s took %ds (%.2fs/elem)", progress(), '%', context.remoteService.url, dt, (float)dt/segment.get().elements.length));
    } catch (FeignException |IOException |InterruptedException e) {
      handleError(context, segment, e);
    }
  }

  private void doRequest(ExecutorContext context, Optional<MatrixDistancesSegment> segment, ResponseCollectorImpl.Distance distanceRequest) throws IOException, InterruptedException {
    log.info("[POST REQUEST] {}", context.remoteService.url);
    RemoteTask remoteTask = distanceRequest.distance(segment.get());
    DistancesRequestResult result = waitForResult(remoteTask.id, context.remoteService.url);
    context.responseCollector.add(segment.get().withDistances(result.distances));
    context.serviceStatusListener.serviceProvidedTaskResult(context.remoteService);
  }

  private void handleError(ExecutorContext context, Optional<MatrixDistancesSegment> segment, Exception e) {
    context.requestsProvider.failedToHandleRequest(getKeyByValue(idMap, segment.get()));
    if (e instanceof FeignException) {
      int status = ((FeignException) e).status();
      if (status == 503) {
        context.serviceStatusListener.serviceCanNotHandleMoreTasks(context.remoteService);
        log.info("Service {} can not handle more tasks", context.remoteService.url, status, e.getMessage());
      } else {
        log.error("Request to {} failed with status {}: {}", context.remoteService.url, status, e.getMessage());
      }
    } else {
      log.error("Request to {} failed: {}", context.remoteService.url, e.getMessage());
    }
  }

  private ResponseCollectorImpl.Distance buildFeignRequest(ExecutorContext context) {
    return Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
           .target(ResponseCollectorImpl.Distance.class, context.remoteService.url);
  }

  private DistancesRequestResult waitForResult(String taskId, String remoteServiceUrl) throws IOException, InterruptedException {
    WaitingPeriod waitingPeriod = new WaitingPeriod();
    ResponseCollectorImpl.DistanceResult distanceResult = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(ResponseCollectorImpl.DistanceResult.class, remoteServiceUrl);
    Response response;
    do {
      waitingPeriod.sleep();
      response = distanceResult.distanceResult(taskId);
    } while (response.status() == 204);

    return (DistancesRequestResult) (new JacksonDecoder())
            .decode(response, DistancesRequestResult.class);
  }

  private int progress() {
    return 100 - ((segmentsToCompute.size() * 100) / idMap.size());
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
