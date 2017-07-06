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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public void requestNotReadable(HttpServletResponse res, HttpMessageNotReadableException e) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String error = "Request body not readable: " + e;
    res.sendError(HttpStatus.BAD_REQUEST.value(), error);
    res.getWriter().write(mapper.writeValueAsString(ErrorResult.error(error)));
  }

  @ExceptionHandler(Exception.class)
  public void internalError(HttpServletResponse res, Exception e) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String error = "Internal error: " + e;
    res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), error);
    res.getWriter().write(mapper.writeValueAsString(ErrorResult.error(error)));
  }
}
