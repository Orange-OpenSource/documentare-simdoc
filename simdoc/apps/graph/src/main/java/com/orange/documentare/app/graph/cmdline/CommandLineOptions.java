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

import com.orange.documentare.core.system.CommandLineException;
import lombok.Getter;
import org.apache.commons.cli.*;

import java.io.File;

@Getter
public class CommandLineOptions {

  private static final String HELP = "h";
  private static final String GRAPH_INPUT_FILE = "json";
  private static final String FILES_ID_MAP = "map";
  private static final String IMAGE_DIRECTORY = "d";

  private static final Options options = new Options();

  private File graphJsonFile;
  private File filesIdMap;
  private String imageDirectory;

  public CommandLineOptions(String[] args) throws ParseException {
    init(args);
  }

  public boolean hasFilesIdMap() {
    return filesIdMap != null;
  }

  public boolean hasImageDirectory() {
    return imageDirectory != null;
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
    checkInputFiles(commandLine);
  }

  private void checkInputFiles(CommandLine commandLine) {
    if (!commandLine.hasOption(GRAPH_INPUT_FILE)) {
      throw new CommandLineException("\nERROR: an input file argument is missing\n");
    } else {
      setInputFiles(commandLine);
    }
  }

  private void setInputFiles(CommandLine commandLine) {
    String trianglesJsonPath = commandLine.getOptionValue(GRAPH_INPUT_FILE);
    if (trianglesJsonPath == null) {
      throw new CommandLineException("\nERROR: an input argument is invalid\n");
    } else {
      doSetInputFiles(trianglesJsonPath, commandLine.getOptionValue(FILES_ID_MAP), commandLine.getOptionValue(IMAGE_DIRECTORY));
    }
  }

  private void doSetInputFiles(String trianglesJsonPath, String filesIdMapJsonPath, String imageDirectoryPath) {
    boolean error = true;
    if (trianglesJsonPath != null) {
      graphJsonFile = new File(trianglesJsonPath);
      error = !graphJsonFile.isFile();
    }
    if (error) {
      throw new CommandLineException("\nERROR: an input file is not accessible\n");
    }
    if (filesIdMapJsonPath != null) {
      File file = new File(filesIdMapJsonPath);
      if (file.exists()) {
        filesIdMap = file;
      }
    }
    if (imageDirectoryPath != null) {
      File file = new File(imageDirectoryPath);
      if (file.isDirectory()) {
        imageDirectory = imageDirectoryPath;
      }
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");
    Option graphJsonOpt = OptionBuilder.withArgName("graph json").hasArg().withDescription("path to json gzip file containing graph").create(GRAPH_INPUT_FILE);
    Option filesIdMapOpt = OptionBuilder.withArgName("files id map").hasArg().withDescription("path to json gzip file containing files id map").create(FILES_ID_MAP);
    Option imageDir = OptionBuilder.withArgName("image directory").hasArg().withDescription(String.format("directory containing images of vertices")).create(IMAGE_DIRECTORY);
    options.addOption(help);
    options.addOption(graphJsonOpt);
    options.addOption(filesIdMapOpt);
    options.addOption(imageDir);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  public static void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("Graph", options);
  }
}
