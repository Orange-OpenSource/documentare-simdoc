package com.orange.documentare.core.comp.clustering.graph.jgrapht.dotexport;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

public class DOT {
  public static String getDOTVertexName(String name) {
    return "g_" + getWithoutAnyExtension(name);
  }

  public static String getWithoutAnyExtension(String string) {
    return string.replace(".", "_");
  }
}
