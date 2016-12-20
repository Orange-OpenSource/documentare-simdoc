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

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class JsonGenericHandlerTest {

  @Test
  public void shouldExportImport() throws IOException {
    // given
    JsonGenericHandler jsonHandler = new JsonGenericHandler(true);
    TestClass testObject = new TestClass();
    File file = new File("test.json.gz");

    // do
    jsonHandler.writeObjectToJsonGzipFile(testObject, file);
    TestClass readObject = (TestClass) jsonHandler.getObjectFromJsonGzipFile(TestClass.class, file);

    // then
    Assert.assertEquals(2, readObject.getArray()[0][1]);
    Assert.assertEquals("b", readObject.getStrings()[1]);
  }
}
