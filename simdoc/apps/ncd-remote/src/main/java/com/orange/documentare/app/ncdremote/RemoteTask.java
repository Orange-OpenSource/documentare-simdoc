package com.orange.documentare.app.ncdremote;

/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RemoteTask {
  public final String id;

  // for deserialization framework
  public RemoteTask() {
    this.id = "";
  }
}
