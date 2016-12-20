package com.orange.documentare.app.compresswords.cmdline;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import lombok.Getter;
import org.apache.commons.cli.*;

import java.io.File;

@Getter
public class CommandLineOptions {

  private static final String HELP = "h";
  private static final String INPUT_FILE = "i";

  private final Options options = new Options();

  private File inputFile;

  public CommandLineOptions(String[] args) throws ParseException {
    init(args);
  }

  private void init(String[] args) throws ParseException {
    CommandLine commandLine = getCommandLineFromArgs(args);
    boolean helpRequested = commandLine.hasOption(HELP);
    if (helpRequested) {
      showHelp();
      throw new IllegalArgumentException("\nPrint this help message\n");
    } else {
      initOptionsValues(commandLine);
    }
  }

  private void initOptionsValues(CommandLine commandLine) {
    if (!commandLine.hasOption(INPUT_FILE)) {
      showHelp();
      throw new IllegalArgumentException("\nERROR: an input file argument is missing\n");
    } else {
      setInputFiles(commandLine);
    }
  }

  private void setInputFiles(CommandLine commandLine) {
    String inputFilePath = commandLine.getOptionValue(INPUT_FILE);
    if (inputFilePath == null) {
      showHelp();
      throw new IllegalArgumentException("\nERROR: an input argument is invalid\n");
    } else {
      doSetInputFiles(inputFilePath);
    }
  }

  private void doSetInputFiles(String inputFilePath) {
    boolean error = true;
    if (inputFilePath != null) {
      inputFile = new File(inputFilePath);
      error = !inputFile.exists();
    }
    if (error) {
      showHelp();
      throw new IllegalArgumentException("\nERROR: an input file is not accessible\n");
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");
    Option inputFileOpt = OptionBuilder.withArgName("input file").hasArg().withDescription("path to the file input file").create(INPUT_FILE);
    options.addOption(help);
    options.addOption(inputFileOpt);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  private void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("Words compression", options);
  }
}
