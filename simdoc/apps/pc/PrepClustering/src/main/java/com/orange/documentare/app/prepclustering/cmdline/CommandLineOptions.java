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

@Getter
@Log4j2
public class CommandLineOptions {

  private static final String HELP = "help";
  private static final String FILE = "f";
  private static final String WRITE_CSV = "writeCSV";
  private static final String KNN = "k";

  private static final Options options = new Options();

  private File file;
  private boolean writeCSV;
  private int kNearestNeighbours = - 1;

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
    writeCSV = commandLine.hasOption(WRITE_CSV);
    if (commandLine.hasOption(KNN)) {
      kNearestNeighbours = Integer.parseInt(commandLine.getOptionValue(KNN));
    }
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
    Option csv = new Option(WRITE_CSV, "write WRITE_CSV files (matrix, nearest)");
    Option file = OptionBuilder.withArgName("file path").hasArg().withDescription("NCD input file").create(FILE);
    Option knn = OptionBuilder.withArgName("knn").hasArg().withDescription("k nearest neighbours").create(KNN);
    options.addOption(help);
    options.addOption(csv);
    options.addOption(file);
    options.addOption(knn);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  public static void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("prepclustering ", options);
  }
}
