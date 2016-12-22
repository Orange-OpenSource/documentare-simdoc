package com.orange.documentare.simdoc.server.biz;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimdocResult {
  public final boolean error;
  public final String errorMessage;

  public static SimdocResult error(String errorMessage) {
    return new SimdocResult(true, errorMessage);
  }
}
