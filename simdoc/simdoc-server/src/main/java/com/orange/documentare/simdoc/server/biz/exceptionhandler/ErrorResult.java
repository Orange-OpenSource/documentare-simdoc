package com.orange.documentare.simdoc.server.biz.exceptionhandler;

/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorResult {
  public final boolean error;
  public final String errorMessage;

  static ErrorResult error(String errorMessage) {
    return new ErrorResult(true, errorMessage);
  }
}
