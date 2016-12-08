package com.orange.documentare.core.comp.clustering.graph.scissors;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.clustering.graph.GraphEdge;
import com.orange.documentare.core.model.ref.clustering.graph.GraphGroup;

public interface ScissorTrigger {
  /**
   * Initialize trigger statistical data for this group
   * @param group
   */
  void initForGroup(GraphGroup group);

  /**
   * Based on trigger statistical data, determine if this edge should be removed
   * @param edge
   * @return true if this edge should be removed
   */
  boolean shouldRemove(GraphEdge edge);
}
