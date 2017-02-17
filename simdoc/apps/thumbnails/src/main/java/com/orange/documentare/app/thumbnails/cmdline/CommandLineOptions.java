package com.orange.documentare.app.thumbnails.cmdline;
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

  private static final Options options = new Options();

  private static final String HELP = "h";
  private static final String DIRECTORY = "d";

  private File directory;

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
    boolean dOption = commandLine.hasOption(DIRECTORY);
    if (!dOption) {
      throw new CommandLineException("\nERROR: input directory argument is missing\n");
    } else {
      setInputFiles(commandLine);
    }
  }

  private void setInputFiles(CommandLine commandLine) {
    String dPath = commandLine.getOptionValue(DIRECTORY);
    if (dPath == null) {
      throw new CommandLineException("\nERROR: input directory is invalid\n");
    } else {
      directory = new File(dPath);
      if (!directory.isDirectory()) {
        throw new CommandLineException("\nERROR: input directory is invalid\n");
      }
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");
    Option d = OptionBuilder.withArgName("directory path").hasArg().withDescription("directory").create(DIRECTORY);
    options.addOption(help);
    options.addOption(d);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  public static void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("thumbnails", options);
  }
}
