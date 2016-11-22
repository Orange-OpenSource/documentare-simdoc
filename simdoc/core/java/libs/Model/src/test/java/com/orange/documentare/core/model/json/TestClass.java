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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
class TestClass {
  private final int[][] array = new int[][] { { 1, 2}, { 3, 4} };
  private final String[] strings = { "a", "b"};
}
