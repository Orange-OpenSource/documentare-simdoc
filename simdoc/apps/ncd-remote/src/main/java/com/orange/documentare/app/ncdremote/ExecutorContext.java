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

@RequiredArgsConstructor
public class ExecutorContext {
  public final RequestsProvider requestsProvider;
  public final ResponseCollector responseCollector;
  public final RemoteService remoteService;
  public final ServiceStatusListener serviceStatusListener;
  public final long threadId;

  private ExecutorContext() {
    // hide direct ctor: use builder instead
    requestsProvider = null;
    responseCollector = null;
    remoteService = null;
    serviceStatusListener = null;
    threadId = -1;
  }

  public static ExecutorContextBuilder builder() {
    return new ExecutorContextBuilder();
  }

  public static class ExecutorContextBuilder {
    private RequestsProvider requestsProvider;
    private ResponseCollector responseCollector;
    private RemoteService remoteService;
    private ServiceStatusListener serviceStatusListener;
    private long threadId;

    private ExecutorContextBuilder() {
      // hide ctor
    }

    public ExecutorContextBuilder requestsProvider(RequestsProvider requestsProvider) {
      this.requestsProvider = requestsProvider;
      return this;
    }
    public ExecutorContextBuilder responseCollector(ResponseCollector responseCollector) {
      this.responseCollector = responseCollector;
      return this;
    }
    public ExecutorContextBuilder remoteService(RemoteService remoteService) {
      this.remoteService = remoteService;
      return this;
    }
    public ExecutorContextBuilder serviceStatusListener(ServiceStatusListener serviceStatusListener) {
      this.serviceStatusListener = serviceStatusListener;
      return this;
    }
    public ExecutorContextBuilder threadId(long threadId) {
      this.threadId = threadId;
      return this;
    }

    public ExecutorContext build() {
      return new ExecutorContext(requestsProvider, responseCollector, remoteService, serviceStatusListener, threadId);
    }
  }
}
