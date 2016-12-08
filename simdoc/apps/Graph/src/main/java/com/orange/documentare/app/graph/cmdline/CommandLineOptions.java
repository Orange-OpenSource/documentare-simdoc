package com.orange.documentare.app.graph.cmdline;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.common.CommandLineException;
import lombok.Getter;
import org.apache.commons.cli.*;

import java.io.File;

@Getter
public class CommandLineOptions {

  private static final String HELP = "h";
  private static final String GRAPH_INPUT_FILE = "json";
  private static final String IMAGE_DIRECTORY = "d";

  private static final Options options = new Options();

  private File graphJsonFile;
  private String imageDirectory = ".";

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
    if (commandLine.hasOption(IMAGE_DIRECTORY)) {
      imageDirectory = commandLine.getOptionValue(IMAGE_DIRECTORY);
    }
    checkInputFiles(commandLine);
  }

  private void checkInputFiles(CommandLine commandLine) {
    if (!commandLine.hasOption(GRAPH_INPUT_FILE)) {
      showHelp();
      throw new CommandLineException("\nERROR: an input file argument is missing\n");
    } else {
      setInputFiles(commandLine);
    }
  }

  private void setInputFiles(CommandLine commandLine) {
    String trianglesJsonPath = commandLine.getOptionValue(GRAPH_INPUT_FILE);
    if (trianglesJsonPath == null) {
      showHelp();
      throw new CommandLineException("\nERROR: an input argument is invalid\n");
    } else {
      doSetInputFiles(trianglesJsonPath);
    }
  }

  private void doSetInputFiles(String trianglesJsonPath) {
    boolean error = true;
    if (trianglesJsonPath != null) {
      graphJsonFile = new File(trianglesJsonPath);
      error = !graphJsonFile.isFile();
    }
    if (error) {
      showHelp();
      throw new CommandLineException("\nERROR: an input file is not accessible\n");
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");
    Option graphJsonOpt = OptionBuilder.withArgName("graph json gzip file path").hasArg().withDescription("path to json gzip file containing graph").create(GRAPH_INPUT_FILE);
    Option imageDir = OptionBuilder.withArgName("image directory").hasArg().withDescription(String.format("directory containing images of vertices")).create(IMAGE_DIRECTORY);
    options.addOption(help);
    options.addOption(graphJsonOpt);
    options.addOption(imageDir);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  public static void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("Graph supporter of the Mouse liberation front, free Mickey from watches!!!", options);
  }
}
