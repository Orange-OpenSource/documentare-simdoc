package com.orange.documentare.core.model.ref.multisets;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
public class MultiSets extends ArrayList<MultiSet> {

  public MultiSets(Collection<MultiSet> multiSets) {
    super(multiSets);
  }
}
