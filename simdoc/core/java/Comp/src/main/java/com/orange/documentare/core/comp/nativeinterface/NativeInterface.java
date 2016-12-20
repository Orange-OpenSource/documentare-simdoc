
package com.orange.documentare.core.comp.nativeinterface;/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public final class NativeInterface {

  /**
   * Launch in command line mode the executable 'exe'
   * @param exe command line executable
   */
  public static void launch(@NonNull String exe) {
    launch(exe, null, null);
  }

  /**
   * Launch in command line mode the executable 'exe'
   * @param exe command line executable
   * @param args command line arguments
   */
  public static void launch(@NonNull String exe, String [] args) {
    launch(exe, args, null);
  }

  /**
   * Launch in command line mode the executable 'exe', with arguments 'args'
   * @param exe command line executable
   * @param args command line arguments
   * @param logFilePath command output log file
   */
  public static void launch(@NonNull String exe, String [] args, String logFilePath) {
    String cmd = getCmd(exe, args, logFilePath);
    log.info(String.format("Start command line: '%s'", cmd));
    try {
      launchCmd(cmd);
    } catch (IOException | InterruptedException e) {
      throwException(cmd, e);
    }
  }

  private static void launchCmd(String cmd) throws InterruptedException, IOException {
    String[] cmdArray = {"/bin/sh", "-c", cmd};
    Process proc = Runtime.getRuntime().exec(cmdArray);
    proc.waitFor();
    if (proc.exitValue() != 0) {
      throw new NativeException(String.format("Command line '%s' returned error code %d", cmd, proc.exitValue()));
    }
  }

  private static String getCmd(String exe, String[] args, String logFilePath) {
    StringBuilder sb = new StringBuilder();
    if (args != null) {
      sb.append(' ');
      for (String s : args) {
        sb.append(s).append(' ');
      }
    }
    return String.format("%s%s%s", exe, sb.toString(), logFilePath == null ? "" : " > " + logFilePath);
  }

  private static void throwException(String cmd, Exception e) {
    String msg = String.format("Failed to launch command line: %s / exception msg = %s", cmd, e.getMessage());
    log.error(msg, e);
    throw new NativeException(msg);
  }
}
