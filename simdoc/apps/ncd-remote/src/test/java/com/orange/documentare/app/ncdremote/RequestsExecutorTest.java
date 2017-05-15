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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class RequestsExecutorTest {
  private static final int REQUESTS_COUNT = 100;

  @RequiredArgsConstructor
  private class Executor implements RequestExecutor {
    /** request data: here only an id */
    private final int id;
    @Override
    /** for test purpose, just return a response with the id */
    public void exec(ExecutorContext context) {
      // Simulate error
      if (context.threadId % 10 == 0) {
        context.requestsProvider.failedToHandleRequest(id);
      } else {
        context.responseCollector.add(new Response(context.threadId, id, context.remoteService.url));
      }
    }
  }

  @RequiredArgsConstructor
  private class Response {
    final long threadId;
    final int requestId;
    final String service;
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

  private AvailableRemoteServices buildAvailableRemoteServices() {
    return new AvailableRemoteServices() {
      private int threadsCount = 1;
      private int zeroLoops;
      @Override
      /** increment threads count, reset it, and simulate 'no threads available' case */
      public void update() {
        if (threadsCount >= 16) {
          threadsCount = 0;
        }
        if (threadsCount == 0) {
          zeroLoops++;
        }
        if (zeroLoops == 0 || zeroLoops > 2) {
          threadsCount += 15;
        }
      }
      @Override
      public int threadsCount() {
        return threadsCount;
      }

      @Override
      public List<RemoteService> services() {
        return IntStream.range(0, threadsCount)
                .mapToObj(i -> new RemoteService("http://server" + i))
                .collect(Collectors.toList());
      }
    };
  }

  /** request runner just add requestId to response collector */
  private RequestsProvider buildRequestProvider() {
    return new RequestsProvider() {
      private final List<Executor> executors = initExecutors();

      private List<Executor> initExecutors() {
        List<Executor> executorsList = new ArrayList<>();
        IntStream.range(0, REQUESTS_COUNT).forEach(
                i -> executorsList.add(new Executor(i))
        );
        return executorsList;
      }

      @Override
      public synchronized Optional<RequestExecutor> getPendingRequestExecutor() {
        if (empty()) {
          return Optional.empty();
        }
        Executor executor = executors.get(0);
        executors.remove(0);
        return Optional.of(executor);
      }

      @Override
      public void failedToHandleRequest(int requestId) {
        log.warn("[ERROR] on request '{}', we will retry with another thread", requestId);
        executors.add(new Executor(requestId));
      }

      @Override
      public synchronized boolean empty() {
        return executors.isEmpty();
      }
    };
  }

  private class TestResponseCollector implements ResponseCollector<Response> {
    private List<Response> responses = new ArrayList<>();

    @Override
    public synchronized void add(Response response) {
      responses.add(response);
      log.info("[Thread {}] request {} @ {}", response.threadId, response.requestId, response.service);
    }

    @Override
    public List<Response> responses() {
      return null;
    }

    public int get(int requestId) {
      Optional<Response> optional = responses.stream()
              .filter(response -> response.requestId == requestId)
              .findFirst();
      return optional.isPresent() ? optional.get().requestId : -1;
    }
  }
}
