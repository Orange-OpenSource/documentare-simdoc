package com.orange.documentare.app.multisets.cmdline;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.multisets.MultiSetsBuilder;
import lombok.Getter;
import org.apache.commons.cli.*;

import java.io.File;

@Getter
public class CommandLineOptions {

  private static final String HELP = "h";
  private static final String EXCLUDE_PROPAGATED = "nop";
  private static final String SEG_INPUT_FILE = "seg";
  private static final String TEXT_INPUT_FILE = "text";
  private static final String CLASS_MIN_SIZE = "min";
  private static final String CLASS_MAX_SIZE = "max";

  private final Options options = new Options();

  private File segmentationInputFile;
  private File textInputFile;
  private int classMinSize = MultiSetsBuilder.CLASS_SIZE_MIN_DEFAULT;
  private int classMaxSize = -1;
  private boolean propagatedCharIncluded = true;

  public CommandLineOptions(String[] args) throws ParseException {
    init(args);
  }

  public boolean isClassMaxSizeAvailable() {
    return classMaxSize > 0;
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
    if (!commandLine.hasOption(SEG_INPUT_FILE) || !commandLine.hasOption(TEXT_INPUT_FILE)) {
      showHelp();
      throw new IllegalArgumentException("\nERROR: an input file argument is missing\n");
    } else {
      setInputFiles(commandLine);
      setClassMinMaxSize(commandLine);
      propagatedCharIncluded = !commandLine.hasOption(EXCLUDE_PROPAGATED);
    }
  }

  private void setInputFiles(CommandLine commandLine) {
    String segInputFilePath = commandLine.getOptionValue(SEG_INPUT_FILE);
    String textInputFilePath = commandLine.getOptionValue(TEXT_INPUT_FILE);
    if (segInputFilePath == null || textInputFilePath == null) {
      showHelp();
      throw new IllegalArgumentException("\nERROR: an input argument is invalid\n");
    } else {
      doSetInputFiles(segInputFilePath, textInputFilePath);
    }
  }

  private void doSetInputFiles(String segInputFilePath, String textInputFilePath) {
    boolean error = true;
    if (segInputFilePath != null && textInputFilePath != null) {
      segmentationInputFile = new File(segInputFilePath);
      textInputFile = new File(textInputFilePath);
      error = !segmentationInputFile.exists() || !textInputFile.exists();
    }
    if (error) {
      showHelp();
      throw new IllegalArgumentException("\nERROR: an input file is not accessible\n");
    }
  }

  private void setClassMinMaxSize(CommandLine commandLine) {
    try {
      if (commandLine.hasOption(CLASS_MIN_SIZE)) {
        classMinSize = Integer.parseInt(commandLine.getOptionValue(CLASS_MIN_SIZE));
      }
      if (commandLine.hasOption(CLASS_MAX_SIZE)) {
        classMaxSize = Integer.parseInt(commandLine.getOptionValue(CLASS_MAX_SIZE));
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("\nERROR: invalid class min/max size\n");
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");
    Option excludePropagatedOpt = new Option(EXCLUDE_PROPAGATED, "exclude propagated chars");
    Option segInputFileOpt = OptionBuilder.withArgName("segmentation input file").hasArg().withDescription("path to the segmentation input file").create(SEG_INPUT_FILE);
    Option textInputFileOpt = OptionBuilder.withArgName("text input file").hasArg().withDescription("path to the text input file").create(TEXT_INPUT_FILE);
    Option classMinSizeOpt = OptionBuilder.withArgName("class min size").hasArg().withDescription("class minimum size to accept to create a multiset").create(CLASS_MIN_SIZE);
    Option classMaxSizeOpt = OptionBuilder.withArgName("class max size").hasArg().withDescription("class maximum number of elements for a multiset").create(CLASS_MAX_SIZE);
    options.addOption(help);
    options.addOption(excludePropagatedOpt);
    options.addOption(segInputFileOpt);
    options.addOption(textInputFileOpt);
    options.addOption(classMinSizeOpt);
    options.addOption(classMaxSizeOpt);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  private void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("Multisets", options);
  }
}
