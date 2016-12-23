package com.orange.documentare.simdoc.server.biz.exceptionhandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorResult {
  public final boolean error;
  public final String errorMessage;

  static ErrorResult error(String errorMessage) {
    return new ErrorResult(true, errorMessage);
  }
}
