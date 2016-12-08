package com.orange.documentare.app.bwt.cmdline;
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
  private static final String HELP = "help";
  private static final String INPUT = "i";
  private static final String OUTPUT = "o";

  private final Options options = new Options();

  private File input;
  private File output;

  public CommandLineOptions(String[] args) throws ParseException {
    init(args);
  }

  private void init(String[] args) throws ParseException {
    CommandLine commandLine = getCommandLineFromArgs(args);
    boolean helpRequested = commandLine.hasOption(HELP);
    if (helpRequested) {
      showHelp();
      throw new CommandLineException("\nPrint this help message\n");
    } else {
      initOptionsValues(commandLine);
    }
  }

  private void initOptionsValues(CommandLine commandLine) {
    boolean inputOption = commandLine.hasOption(INPUT);
    boolean outputOption = commandLine.hasOption(OUTPUT);
    if (!inputOption || !outputOption) {
      showHelp();
      throw new CommandLineException("\nERROR: input/output file argument missing\n");
    } else {
      setInputFiles(commandLine);
    }
  }

  private void setInputFiles(CommandLine commandLine) {
    String inputPath = commandLine.getOptionValue(INPUT);
    String outputPath = commandLine.getOptionValue(OUTPUT);

    if ((inputPath == null) || (outputPath == null)) {
      showHelp();
      throw new CommandLineException("\nERROR: input/output file name is invalid\n");
    } else {
      doSetIOFiles(inputPath, outputPath);
    }
  }

  private void doSetIOFiles(String inputPath, String outputPath) {
    boolean error;
    input = new File(inputPath);
    output = new File(outputPath);
    error = !input.exists();
    if (error) {
      showHelp();
      throw new CommandLineException("\nERROR: input file is not accessible\n");
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");
    Option file1 = OptionBuilder.withArgName("file path").hasArg().withDescription("input file").create(INPUT);
    Option file2 = OptionBuilder.withArgName("file path").hasArg().withDescription("output file").create(OUTPUT);
    options.addOption(help);
    options.addOption(file1);
    options.addOption(file2);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  private void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("bwt for u+yvo+o", options);
  }
}
