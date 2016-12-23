package com.orange.documentare.simdoc.server.biz.exceptionhandler;

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
  public void handleError(HttpServletResponse res, HttpMessageNotReadableException e) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String error = "Request body not readable: " + e;
    res.sendError(HttpStatus.BAD_REQUEST.value(), error);
    res.getWriter().write(mapper.writeValueAsString(ErrorResult.error(error)));
  }
}
