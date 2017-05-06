package com.orange.documentare.app.ncdremote;

public interface RequestExecutor {
  void exec(RequestsProvider requestsProvider, ResponseCollector responseCollector, int threadId);
}
