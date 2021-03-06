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
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HighOrderExceptionHandler {

  @ExceptionHandler(UnrecognizedPropertyException.class)
  public void handleError(HttpServletResponse res, UnrecognizedPropertyException e) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String error = "Request JSON body contains an unknown property: " + e;
    res.sendError(HttpStatus.BAD_REQUEST.value(), error);
    res.getWriter().write(mapper.writeValueAsString(ErrorResult.error(error)));
  }
}
