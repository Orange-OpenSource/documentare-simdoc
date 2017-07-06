package com.orange.documentare.app.prepdata.cmdline;
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
  private static final String WITH_BYTES = "bytes";
  private static final String BYTES_SIZE = "bsize";

  private static final Options options = new Options();

  private File inputDir;
  private boolean rawConverterEnabled;
  private boolean withBytes;
  private int expectedBytesSize;

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
    withBytes = commandLine.hasOption(WITH_BYTES);
    expectedBytesSize = commandLine.hasOption(BYTES_SIZE) ? Integer.parseInt(commandLine.getOptionValue(BYTES_SIZE)) : 0;
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
    Option withBytes = new Option(WITH_BYTES, "embed bytes instead of filepath in BytesData");
    Option bytesSize = OptionBuilder.withArgName("bytes size").hasArg().withDescription("expected raw bytes size").create(BYTES_SIZE);
    options.addOption(help);
    options.addOption(file);
    options.addOption(raw);
    options.addOption(withBytes);
    options.addOption(bytesSize);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  public static void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("prep-data ", options);
  }
}
