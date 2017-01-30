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

import com.orange.documentare.core.system.nativeinterface.NativeException;
import com.orange.documentare.core.system.nativeinterface.NativeInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

@Slf4j
public class NativeInterfaceTest {
  private static final String CMD_LOG = "cmd.log";

  @After
  public void cleanup() {
    FileUtils.deleteQuietly(new File(CMD_LOG));
  }

  @Test
  public void shouldRaiseNonNullExceptionOnNullExe() {
    // given
    boolean gotException = false;
    // then
    try {
      NativeInterface.launch(null, null, null);
    } catch(NullPointerException e) {
      log.info(e.getMessage(), e);
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
      log.info(e.getMessage());
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
      log.info(e.getMessage());
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
    String logPath = CMD_LOG;
    boolean gotException = false;
    // then
    try {
      NativeInterface.launch(exe, args, logPath);
    } catch(NativeException e) {
      log.info(e.getMessage(), e);
      gotException = true;
    }
    if (gotException) {
      Assert.fail();
    }
  }
}
