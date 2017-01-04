package com.orange.documentare.core.model.alto;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.orange.documentare.core.model.alto.ref.page.PageRef;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public final class JsonHelper {

  private JsonHelper() {
    // Hide Utility Class Constructor
  }

  /**
   * Serialize a Java object to a Json String
   *
   * @param doc the java Object to serialize to a Json String
   * @param prettyPrint
   * @return the Json String
   * @throws java.io.IOException
   */
  public static String getJson ( final Object doc, final boolean prettyPrint ) throws IOException {

    final ObjectMapper mapper = new ObjectMapper();

    mapper.setSerializationInclusion(Include.NON_NULL);
    mapper.setSerializationInclusion(Include.NON_EMPTY);

    String json;
    if (prettyPrint) {
      final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
      json = writer.writeValueAsString(doc);
    } else {
      json = mapper.writeValueAsString(doc);
    }

    return json;
  }

  /**
   * Do the data binding from a Json string to a PageRef java Object
   *
   * @param json Json String
   * @return a PageRef Object
   * @throws java.io.IOException
   */
  public static PageRef getPageRef ( final String json ) throws IOException {

    final ObjectMapper mapper = new ObjectMapper();

    // To let Image Processing / Portal add metadatas
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    try {
      return mapper.readValue(json, PageRef.class);
    } catch (final JsonProcessingException e) {
      log.error(String.format("[FATAL] getPageRef, failed to parse json string: %s", e.getMessage()));
      throw e;
    }
  }

}
