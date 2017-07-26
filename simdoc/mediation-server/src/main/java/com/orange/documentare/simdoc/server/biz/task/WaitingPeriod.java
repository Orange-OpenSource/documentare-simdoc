package com.orange.documentare.simdoc.server.biz.task;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

public class WaitingPeriod {
  private static final int PERIOD_MAX_S = 60 * 1000;

  private int periodMilliseconds = 1000;

  public void sleep() throws InterruptedException {
    int period =  Math.min(periodMilliseconds, PERIOD_MAX_S);
    Thread.sleep(period);
    periodMilliseconds += 5000;
  }
}
