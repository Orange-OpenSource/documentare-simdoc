package com.orange.documentare.app.ocr.cmdline;
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
  private static final String SEG_INPUT_FILE = "seg";
  private static final String MULTISETS_INPUT_FILE = "msets";

  private final Options options = new Options();

  private File segmentationInputFile;
  private File multisetsInputFile;

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
    if (!commandLine.hasOption(SEG_INPUT_FILE) || !commandLine.hasOption(MULTISETS_INPUT_FILE)) {
      showHelp();
      throw new IllegalArgumentException("\nERROR: an input file argument is missing\n");
    } else {
      setInputFiles(commandLine);
    }
  }

  private void setInputFiles(CommandLine commandLine) {
    String segInputFilePath = commandLine.getOptionValue(SEG_INPUT_FILE);
    String mSetsInputFilePath = commandLine.getOptionValue(MULTISETS_INPUT_FILE);
    if (segInputFilePath == null || mSetsInputFilePath == null) {
      showHelp();
      throw new IllegalArgumentException("\nERROR: an input argument is invalid\n");
    } else {
      doSetInputFiles(segInputFilePath, mSetsInputFilePath);
    }
  }

  private void doSetInputFiles(String segInputFilePath, String mSetsInputFilePath) {
    boolean error = true;
    if (segInputFilePath != null && mSetsInputFilePath != null) {
      segmentationInputFile = new File(segInputFilePath);
      multisetsInputFile = new File(mSetsInputFilePath);
      error = !segmentationInputFile.exists() || !multisetsInputFile.exists();
    }
    if (error) {
      showHelp();
      throw new IllegalArgumentException("\nERROR: an input file is not accessible\n");
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");
    Option segInputFileOpt = OptionBuilder.withArgName("segmentation input file").hasArg().withDescription("path to the segmentation input file").create(SEG_INPUT_FILE);
    Option mSetsInputFileOpt = OptionBuilder.withArgName("multisets input file").hasArg().withDescription("path to the multisets input file").create(MULTISETS_INPUT_FILE);
    options.addOption(help);
    options.addOption(segInputFileOpt);
    options.addOption(mSetsInputFileOpt);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  private void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("Ocr", options);
  }
}
