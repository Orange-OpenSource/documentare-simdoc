package com.orange.documentare.app.ncdremote;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Denis Boisset & Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import feign.Feign;
import feign.RequestLine;

class KillAllTasks {
  interface IKillAllTasks {
    @RequestLine("POST /kill-all-tasks")
    void killAllTasks();
  }

  static void serialKiller() {
    LocalAvailableRemoteServices services = new LocalAvailableRemoteServices();
    services.services().forEach(service -> kill(service.url));
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      // catch silently
    }
  }

  private static void kill(String url) {
    buildFeignRequest(url)
    .killAllTasks();
  }

  private static IKillAllTasks buildFeignRequest(String url) {
    return Feign.builder()
      .target(KillAllTasks.IKillAllTasks.class, url);
  }
}
