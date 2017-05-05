package com.orange.documentare.app.ncdremote.cmdline;
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

  private static final Options options = new Options();

  private static final String HELP = "h";
  private static final String DIRECTORY_1 = "d1";
  private static final String DIRECTORY_2 = "d2";

  private static final String JSON_1 = "j1";
  private static final String JSON_2 = "j2";

  private static final String SIMDOC_MODE = "simdoc";

  private File d1;
  private File d2;
  private File j1;
  private File j2;
  private File simdoc;

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
    boolean simDocOption = commandLine.hasOption(SIMDOC_MODE);
    boolean d1Option = commandLine.hasOption(DIRECTORY_1);
    boolean j1Option = commandLine.hasOption(JSON_1);

    if (!simDocOption && !d1Option && !j1Option) {
      throw new CommandLineException("\nERROR: an input file argument is missing\n");
    } else {
      setInputFiles(commandLine);
    }
  }

  private void setInputFiles(CommandLine commandLine) {
    String simDocPath = commandLine.getOptionValue(SIMDOC_MODE);
    String d1Path = commandLine.getOptionValue(DIRECTORY_1);
    String d2Path = commandLine.getOptionValue(DIRECTORY_2);
    String j1Path = commandLine.getOptionValue(JSON_1);
    String j2Path = commandLine.getOptionValue(JSON_2);
    if (simDocPath == null && d1Path == null && j1Path == null) {
      throw new CommandLineException("\nERROR: an input file name is invalid\n");
    } else {
      doSetInputFiles(simDocPath, d1Path, d2Path, j1Path, j2Path);
    }
  }

  private void doSetInputFiles(String simDocPath, String file1Path, String file2Path, String j1Path, String j2Path) {
    boolean error;
    if (simDocPath != null) {
      simdoc = new File(simDocPath);
      error = !simdoc.exists();
    } else {
      d1 = file1Path != null ? new File(file1Path) : null;
      d2 = file1Path != null ? new File(file2Path == null ? file1Path : file2Path) : null;
      j1 = j1Path != null ? new File(j1Path) : null;
      j2 = j1Path != null ? new File(j2Path == null ? j1Path : j2Path) : null;
      if (j1Path == null) {
        error = !d1.isDirectory() || !d2.isDirectory();
      } else {
        error = !j1.exists() || !j2.exists();
      }
    }
    if (error) {
      throw new CommandLineException("\nERROR: an input file is not accessible\n");
    }

    if (j1Path == null && file2Path == null) {
      System.out.println("Assumes d2 = d1");
    }
    if (j1Path != null && j2Path == null) {
      System.out.println("Assumes j2 = j1");
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");
    Option d1 = OptionBuilder.withArgName("directory path").hasArg().withDescription("directory 1").create(DIRECTORY_1);
    Option d2 = OptionBuilder.withArgName("directory path").hasArg().withDescription("directory 2, not mandatory (we assume d2=d1)").create(DIRECTORY_2);
    Option j1 = OptionBuilder.withArgName("bytes data json path").hasArg().withDescription("bytes data json 1").create(JSON_1);
    Option j2 = OptionBuilder.withArgName("bytes data json path").hasArg().withDescription("bytes data json 2, not mandatory (we assume j2=j1)").create(JSON_2);
    Option simdoc = OptionBuilder.withArgName("simdoc file path").hasArg().withDescription("SimDoc model .json.gz file path").create(SIMDOC_MODE);
    options.addOption(help);
    options.addOption(d1);
    options.addOption(d2);
    options.addOption(j1);
    options.addOption(j2);
    options.addOption(simdoc);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  public static void showHelp(Exception e) {
    System.out.println();
    System.out.println("Error: " + e.getMessage());
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("ncdremote", options);
  }
}
