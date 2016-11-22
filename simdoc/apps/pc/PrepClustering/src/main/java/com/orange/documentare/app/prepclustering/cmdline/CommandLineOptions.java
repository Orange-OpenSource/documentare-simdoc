package com.orange.documentare.app.prepclustering.cmdline;
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
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.OptionalInt;

@Getter
@Log4j2
public class CommandLineOptions {

  private static final String HELP = "help";
  private static final String FILE = "f";
  private static final String TRIANGLE = "tr";

  private final Options options = new Options();

  private File file;
  private boolean useTriangulationVertices;

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
    boolean fileOption = commandLine.hasOption(FILE);
    if (!fileOption) {
      showHelp();
      throw new CommandLineException("\nERROR: an input file argument is missing\n");
    }
    setInputFiles(commandLine);
    useTriangulationVertices = commandLine.hasOption(TRIANGLE);
  }

  private void setInputFiles(CommandLine commandLine) {
    String filePath = commandLine.getOptionValue(FILE);
    if (filePath == null) {
      showHelp();
      throw new CommandLineException("\nERROR: an input file name is invalid\n");
    } else {
      doSetInputFiles(filePath);
    }
  }

  private void doSetInputFiles(String filePath) {
    file = new File(filePath);
    if (!file.exists()) {
      showHelp();
      throw new CommandLineException("\nERROR: an input file is not accessible\n");
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");
    Option file = OptionBuilder.withArgName("file path").hasArg().withDescription("NCD input file").create(FILE);
    Option useTriangulationVertices = new Option(TRIANGLE, "use triangulation vertices");
    options.addOption(help);
    options.addOption(file);
    options.addOption(useTriangulationVertices);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  private void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("prepclustering ", options);
  }
}
