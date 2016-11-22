package com.orange.documentare.app.compresswords;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.app.compresswords.cmdline.CommandLineOptions;
import com.orange.documentare.core.comp.lyndonrle.LyndonRle;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class WordsCompressor {
  private static CommandLineOptions options;

  public static void main(String[] args) throws IllegalAccessException, IOException, ParseException {
    try {
      options = new CommandLineOptions(args);
      doTheJob(options);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void doTheJob(CommandLineOptions options) throws IOException {
    LyndonRle lyndonRle = new LyndonRle();
    byte[] input = FileUtils.readFileToByteArray(options.getInputFile());
    byte[] compressedBytes = lyndonRle.compress(input);
    FileUtils.writeByteArrayToFile(new File("Lyndon.out"), compressedBytes);
  }
}
