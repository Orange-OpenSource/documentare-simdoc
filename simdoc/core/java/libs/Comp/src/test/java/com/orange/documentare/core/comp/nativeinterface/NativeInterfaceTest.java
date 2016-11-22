package com.orange.documentare.core.comp.nativeinterface;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;

@Log4j2
public class NativeInterfaceTest {

  @Test
  public void shouldRaiseNonNullExceptionOnNullExe() {
    // given
    boolean gotException = false;
    // then
    try {
      NativeInterface.launch(null, null, null);
    } catch(NullPointerException e) {
      log.info(e);
      gotException = true;
    }
    if (!gotException) {
      Assert.fail();
    }
  }

  @Test
  public void shouldRaiseNativeExceptionOnInvalidCommand() {
    // given
    String exe = "dfdfdfdf";
    boolean gotException = false;
    // then
    try {
      NativeInterface.launch(exe, null, null);
    } catch(NativeException e) {
      log.info(e);
      gotException = true;
    }
    if (!gotException) {
      Assert.fail();
    }
  }

  @Test
  public void shouldSuccessWithoutArgsNorLog() {
    // given
    String exe = "ls";
    boolean gotException = false;
    // then
    try {
      NativeInterface.launch(exe, null, null);
    } catch(NativeException e) {
      log.info(e);
      gotException = true;
    }
    if (gotException) {
      Assert.fail();
    }
  }

  @Test
  public void shouldSuccess() {
    // given
    String exe = "ls";
    String[] args = new String[] { ".", ".." };
    String logPath = "cmd.log";
    boolean gotException = false;
    // then
    try {
      NativeInterface.launch(exe, args, logPath);
    } catch(NativeException e) {
      log.info(e);
      gotException = true;
    }
    if (gotException) {
      Assert.fail();
    }
  }
}
