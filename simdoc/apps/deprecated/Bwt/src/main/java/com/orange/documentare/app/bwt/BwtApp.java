package com.orange.documentare.app.bwt;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.bwt.cmdline.CommandLineException;
import com.orange.documentare.app.bwt.cmdline.CommandLineOptions;
import com.orange.documentare.core.comp.bwt.SaisBwt;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import java.io.IOException;

public class BwtApp {

  private static CommandLineOptions commandLineOptions;

  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
    try {
      commandLineOptions = new CommandLineOptions(args);
      doTheJob();
    } catch (CommandLineException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void doTheJob() throws IOException {
    byte[] inputBytes = FileUtils.readFileToByteArray(commandLineOptions.getInput());
    SaisBwt bwt = new SaisBwt();
    byte[] outputBytes = bwt.getBwt(inputBytes);
    FileUtils.writeByteArrayToFile(commandLineOptions.getOutput(), outputBytes);
  }
}
