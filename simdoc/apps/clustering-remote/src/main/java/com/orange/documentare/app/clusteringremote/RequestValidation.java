package com.orange.documentare.app.clusteringremote;

/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RequestValidation {
  public final boolean ok;
  public final String error;
}
