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
import java.util.Optional;

@Getter
public class CommandLineOptions {

  private static final String HELP = "h";
  private static final String GRAPH_INPUT_FILE = "json";
  private static final String METADATA = "metadata";
  private static final String IMAGE_DIRECTORY = "d";
  private static final String THUMBNAILS_SOURCE_DIR = "src";

  private static final Options options = new Options();

  private File graphJsonFile;
  private Optional<File> metadata = Optional.empty();
  private Optional<File> imageDirectory = Optional.empty();
  private Optional<File> thumbnailsSourceDirectory = Optional.empty();

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
      doSetInputFiles(
        trianglesJsonPath,
        commandLine.getOptionValue(METADATA),
        commandLine.getOptionValue(IMAGE_DIRECTORY),
        commandLine.getOptionValue(THUMBNAILS_SOURCE_DIR));
    }
  }

  private void doSetInputFiles(
    String trianglesJsonPath, String metadataJsonPath, String imageDirectoryPath, String thumbnailsSourceDirectoryPath) {
    boolean error = true;
    if (trianglesJsonPath != null) {
      graphJsonFile = new File(trianglesJsonPath);
      error = !graphJsonFile.isFile();
    }
    if (error) {
      throw new CommandLineException("\nERROR: an input file is not accessible\n");
    }
    if (metadataJsonPath != null) {
      File file = new File(metadataJsonPath);
      if (file.exists()) {
        metadata = Optional.of(file);
      }
    }
    if (imageDirectoryPath != null) {
      File file = new File(imageDirectoryPath);
      if (file.isDirectory()) {
        imageDirectory = Optional.of(file);
      }
    }
    if (thumbnailsSourceDirectoryPath != null) {
      File file = new File(thumbnailsSourceDirectoryPath);
      if (file.isDirectory()) {
        thumbnailsSourceDirectory = Optional.of(file);
      }
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");
    Option graphJsonOpt = OptionBuilder.withArgName("graph json").hasArg().withDescription("path to json gzip file containing graph").create(GRAPH_INPUT_FILE);
    Option metadataOpt = OptionBuilder.withArgName("files metadata").hasArg().withDescription("path to json file containing files metadata").create(METADATA);
    Option imageDir = OptionBuilder.withArgName("image directory").hasArg().withDescription(String.format("directory containing images of vertices")).create(IMAGE_DIRECTORY);
    Option thSourceDir = OptionBuilder.withArgName("thumbnails source directory").hasArg().withDescription(String.format("directory containing files to use for thumbnails")).create(THUMBNAILS_SOURCE_DIR);
    options.addOption(help);
    options.addOption(graphJsonOpt);
    options.addOption(metadataOpt);
    options.addOption(imageDir);
    options.addOption(thSourceDir);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  public static void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("Graph", options);
  }
}
