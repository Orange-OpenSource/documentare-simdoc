package com.orange.documentare.core.comp.clustering.graph;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;

import java.util.ArrayList;

public class Items extends ArrayList<GraphItem> {

  public Items() {
    init();
  }

  private void init() {
      GraphItem item1 = new GraphItem();
      GraphItem item2 = new GraphItem();
      GraphItem item3 = new GraphItem();
      item1.setArea(1000);
      item1.setQ(1);
      item2.setArea(10000);
      item2.setQ(1);
      item3.setArea(1000);
      item3.setQ(0.5f);
      add(item1);
      add(item2);
      add(item3);
  }
}
