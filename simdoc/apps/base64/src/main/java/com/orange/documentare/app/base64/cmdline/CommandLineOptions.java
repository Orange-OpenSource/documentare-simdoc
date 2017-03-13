package com.orange.documentare.app.base64.cmdline;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.system.CommandLineException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

import java.io.File;

@Getter
@Slf4j
public class CommandLineOptions {

  private static final String HELP = "h";
  private static final String INPUT_DIR = "d";
  private static final String RAW = "raw";

  private static final Options options = new Options();

  private File inputDir;
  private boolean rawConverterEnabled;

  public CommandLineOptions(String[] args) throws ParseException {
    init(args);
  }

  private void init(String[] args) throws ParseException {
    CommandLine commandLine = getCommandLineFromArgs(args);
    boolean helpRequested = commandLine.hasOption(HELP);
    if (helpRequested) {
      throw new CommandLineException("\nPrint this help message\n");
    } else {
      initOptionsValues(commandLine);
    }
  }

  private void initOptionsValues(CommandLine commandLine) {
    boolean fileOption = commandLine.hasOption(INPUT_DIR);
    if (!fileOption) {
      throw new CommandLineException("\nERROR: input directory argument is missing\n");
    }
    setInputFiles(commandLine);
    rawConverterEnabled = commandLine.hasOption(RAW);
  }

  private void setInputFiles(CommandLine commandLine) {
    String filePath = commandLine.getOptionValue(INPUT_DIR);
    if (filePath == null) {
      throw new CommandLineException("\nERROR: input directory name is invalid\n");
    } else {
      doSetInputFiles(filePath);
    }
  }

  private void doSetInputFiles(String filePath) {
    inputDir = new File(filePath);
    if (!inputDir.isDirectory()) {
      throw new CommandLineException("\nERROR: input directory not accessible\n");
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");
    Option file = OptionBuilder.withArgName("inputDirectory path").hasArg().withDescription("input directory").create(INPUT_DIR);
    Option raw = new Option(RAW, "enable raw conversion for images");
    options.addOption(help);
    options.addOption(file);
    options.addOption(raw);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  public static void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("base64 ", options);
  }
}
