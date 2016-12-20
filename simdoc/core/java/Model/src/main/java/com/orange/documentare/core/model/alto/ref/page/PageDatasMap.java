
package com.orange.documentare.core.model.alto.ref.page;/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */


import java.util.HashMap;
import lombok.NoArgsConstructor;


/**
 * For Json deserialization (Type Erasure)
 */
@NoArgsConstructor
public final class PageDatasMap extends HashMap <String, PageDatas> {
  private static final long serialVersionUID = 1L;
}
