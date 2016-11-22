package com.orange.documentare.core.model.json;
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
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class JsonGenericHandler {
  private boolean prettyPrint;

  @Getter
  private final ObjectMapper mapper = new ObjectMapper();

  public JsonGenericHandler() {
    this(false);
  }

  public JsonGenericHandler(boolean prettyPrint) {
    this.prettyPrint = prettyPrint;
  }

  /**
   * @param object
   * @param file
   * @throws IOException
   */
  public void writeObjectToJsonGzipFile(Object object, File file) throws IOException {
    ObjectWriter writer = prettyPrint ? mapper.writerWithDefaultPrettyPrinter() : mapper.writer();
    GZIPOutputStream outputStream = new GZIPOutputStream(new FileOutputStream(file));
    writer.writeValue(outputStream, object);
    outputStream.close();
  }

  /**
   * @param clazz Object class which will be used to deserialize the Json gzip file
   * @param file
   * @return Object deserialized from json gzip file
   * @throws IOException
   */
  public Object getObjectFromJsonGzipFile(Class clazz, File file) throws IOException {
    GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(file));
    Object object = mapper.readValue(gzipInputStream, clazz);
    gzipInputStream.close();
    return object;
  }

  /**
   * @param object
   * @param file
   * @throws IOException
   */
  public void writeObjectToJsonFile(Object object, File file) throws IOException {
    ObjectWriter writer = prettyPrint ? mapper.writerWithDefaultPrettyPrinter() : mapper.writer();
    OutputStream outputStream = new FileOutputStream(file);
    writer.writeValue(outputStream, object);
    outputStream.close();
  }

  /**
   * @param clazz Object class which will be used to deserialize the Json file
   * @param file
   * @return Object deserialized from json file
   * @throws IOException
   */
  public Object getObjectFromJsonFile(Class clazz, File file) throws IOException {
    InputStream is = new FileInputStream(file);
    Object object = mapper.readValue(is, clazz);
    is.close();
    return object;
  }
}
