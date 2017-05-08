package com.orange.documentare.app.ncdremote;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExecutorContext {
  public final RequestsProvider requestsProvider;
  public final ResponseCollector responseCollector;
  public final RemoteService remoteService;
  public final int threadId;

  private ExecutorContext() {
    // hide direct ctor: use builder instead
    requestsProvider = null;
    responseCollector = null;
    remoteService = null;
    threadId = -1;
  }

  public static ExecutorContextBuilder builder() {
    return new ExecutorContextBuilder();
  }

  public static class ExecutorContextBuilder {
    private RequestsProvider requestsProvider;
    private ResponseCollector responseCollector;
    private RemoteService remoteService;
    private int threadId;

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
    public ExecutorContextBuilder availableRemoteService(RemoteService remoteService) {
      this.remoteService = remoteService;
      return this;
    }
    public ExecutorContextBuilder threadId(int threadId) {
      this.threadId = threadId;
      return this;
    }

    public ExecutorContext build() {
      return new ExecutorContext(requestsProvider, responseCollector, remoteService, threadId);
    }
  }
}
