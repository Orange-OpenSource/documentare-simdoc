package com.orange.documentare.core.comp;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.json.JsonGenericHandler;

import java.io.File;
import java.io.IOException;

public class Res {

  private final Class<?> clazz;

  public Res(Object obj) {
    clazz = obj.getClass();
  }

  /**
   * @param resourceName name of the file under the 'resources' tree
   * @return associate 'File' instance
   */
  public File file(String resourceName) {
    return new File(clazz.getResource(resourceName).getFile());
  }

  /**
   * Deserialize a json to a java Object
   * @param resourceName name of the file under the 'resources' tree
   * @param clazz object class
   * @return deserialized object from json resource
   * @throws IOException
   */
  public Object jsonObj(String resourceName, Class clazz) throws IOException {
    JsonGenericHandler jsonHandler = new JsonGenericHandler(true);
    return jsonHandler.getObjectFromJsonFile(clazz, file(resourceName));
  }
}
