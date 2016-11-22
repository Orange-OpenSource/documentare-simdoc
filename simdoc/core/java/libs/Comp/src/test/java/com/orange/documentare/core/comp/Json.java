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

public class Json {

  public static void toFile(Object obj, String filename) throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);
    jsonGenericHandler.writeObjectToJsonFile(obj, new File(filename));
  }
}
