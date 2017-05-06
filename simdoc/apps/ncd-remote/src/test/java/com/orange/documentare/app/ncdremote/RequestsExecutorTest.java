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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
public class RequestsExecutorTest {
  private static final int REQUESTS_COUNT = 100;

  @RequiredArgsConstructor
  private class Response {
    final int threadId;
    final int requestId;
  }

  @Test
  public void dispatch_requests_with_all_available_microservices() {
    // Given
    TestResponseCollector responseCollector = new TestResponseCollector();
    RequestsProvider requestsProvider = buildRequestProvider();
    AvailableRemoteServices availableRemoteServices = buildAvailableRemoteServices();
    RequestsExecutor requestsExecutor = new RequestsExecutor(requestsProvider, responseCollector, availableRemoteServices);

    // When
    requestsExecutor.exec();

    // Then
    IntStream.range(1, REQUESTS_COUNT).forEach(requestId ->
            Assertions.assertThat(responseCollector.get(requestId)).isEqualTo(requestId)
    );
  }

  /** test impl to increment the number of threads at each call */
  private AvailableRemoteServices buildAvailableRemoteServices() {
    return new AvailableRemoteServices() {
      private int threadsCount = 1;
      private int zeroLoops;
      @Override
      public void update() {
        if (threadsCount >= 8) {
          threadsCount = 0;
        }
        if (threadsCount == 0) {
          zeroLoops++;
        }
        if (zeroLoops == 0 || zeroLoops > 4) {
          threadsCount += 2;
        }
      }
      @Override
      public int threadsCount() {
        return threadsCount;
      }
    };
  }

  /** request runner just add requestId to response collector */
  private RequestsProvider buildRequestProvider() {
    return new RequestsProvider() {
      private int requestId;
      @Override
      public synchronized Optional<RequestExecutor> getPendingRequestExecutor() {
        if (empty()) {
          return Optional.empty();
        }
        requestId++;
        return Optional.of(new RequestExecutor() {
                             private final int id = requestId;
                             @Override
                             public void exec(RequestsProvider requestsProvider, ResponseCollector responseCollector, int threadId) {
                               responseCollector.add(new Response(threadId, id));
                               try {
                                 Thread.sleep(200);
                               } catch (InterruptedException e) {
                                 e.printStackTrace();
                               }
                             }
                           }
        );
      }
      @Override
      public synchronized boolean empty() {
        return requestId >= REQUESTS_COUNT;
      }
    };
  }

  private class TestResponseCollector implements ResponseCollector<Response> {
    private List<Response> responses = new ArrayList<>();

    @Override
    public synchronized void add(Response response) {
      responses.add(response);
      log.info("[Thread {}] request {}", response.threadId, response.requestId);
    }

    public int get(int requestId) {
      Optional<Response> optional = responses.stream()
              .filter(response -> response.requestId == requestId)
              .findFirst();
      return optional.isPresent() ? optional.get().requestId : -1;
    }
  }
}
