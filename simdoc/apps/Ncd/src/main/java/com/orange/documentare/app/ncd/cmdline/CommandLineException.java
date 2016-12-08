package com.orange.documentare.app.ncd.cmdline;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

public class CommandLineException extends RuntimeException {
  public CommandLineException(String message) {
    super(message);
  }
}
