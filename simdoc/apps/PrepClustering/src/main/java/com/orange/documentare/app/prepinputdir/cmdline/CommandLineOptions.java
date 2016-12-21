package com.orange.documentare.app.prepinputdir.cmdline;
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
  private static final String INPUT_JSON_GZ = "json";
  private static final String WRITE_CSV = "writeCSV";
  private static final String KNN = "k";

  private static final Options options = new Options();

  private File inputJsonGz;
  private boolean writeCSV;
  private int kNearestNeighbours = - 1;

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
    boolean fileOption = commandLine.hasOption(INPUT_JSON_GZ);
    if (!fileOption) {
      throw new CommandLineException("\nERROR: an input inputJsonGz argument is missing\n");
    }
    setInputFiles(commandLine);
    writeCSV = commandLine.hasOption(WRITE_CSV);
    if (commandLine.hasOption(KNN)) {
      kNearestNeighbours = Integer.parseInt(commandLine.getOptionValue(KNN));
    }
  }

  private void setInputFiles(CommandLine commandLine) {
    String filePath = commandLine.getOptionValue(INPUT_JSON_GZ);
    if (filePath == null) {
      throw new CommandLineException("\nERROR: an input inputJsonGz name is invalid\n");
    } else {
      doSetInputFiles(filePath);
    }
  }

  private void doSetInputFiles(String filePath) {
    inputJsonGz = new File(filePath);
    if (!inputJsonGz.exists()) {
      throw new CommandLineException("\nERROR: an input inputJsonGz is not accessible\n");
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");
    Option csv = new Option(WRITE_CSV, "write WRITE_CSV files (matrix, nearest)");
    Option file = OptionBuilder.withArgName("inputJsonGz path").hasArg().withDescription("NCD input inputJsonGz").create(INPUT_JSON_GZ);
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
    formatter.printHelp("prepinputdir ", options);
  }
}
