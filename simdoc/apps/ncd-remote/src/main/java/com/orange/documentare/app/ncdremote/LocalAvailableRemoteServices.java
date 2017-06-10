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

import com.google.common.collect.ImmutableList;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class LocalAvailableRemoteServices implements AvailableRemoteServices {
  private final List<RemoteService> availableServices = new ArrayList<>();

  LocalAvailableRemoteServices() {
    //availableServices.add(new RemoteService("http://g-z820-cm:8080"));
    //availableServices.add(new RemoteService("http://g-z620-4lfq:8080"));
    //availableServices.add(new RemoteService("http://g-z440-cm:8080"));
    availableServices.add(new RemoteService("http://localhost:8080"));
  }

  @Override
  public void update() {
    // FIXME, in next version:
    // - here we should clear the available services
    // - and update it through a request the service discovery server
  }

  @Override
  public List<RemoteService> services() {
    return ImmutableList.copyOf(availableServices);
  }
}
