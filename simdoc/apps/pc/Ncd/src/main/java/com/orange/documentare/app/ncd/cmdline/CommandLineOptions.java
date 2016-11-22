package com.orange.documentare.app.ncd.cmdline;
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
  private static final String FILE1 = "file1";
  private static final String FILE2 = "file2";
  private static final String SIMDOC_JSON_GZIP = "simDocJsonGz";

  private final Options options = new Options();

  private File file1;
  private File file2;
  private File simDocJsonGz;

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
    boolean simDocOption = commandLine.hasOption(SIMDOC_JSON_GZIP);
    boolean file1Option = commandLine.hasOption(FILE1);
    if (!simDocOption && !file1Option) {
      showHelp();
      throw new CommandLineException("\nERROR: an input file argument is missing\n");
    } else {
      setInputFiles(commandLine);
    }
  }

  private void setInputFiles(CommandLine commandLine) {
    String simDocPath = commandLine.getOptionValue(SIMDOC_JSON_GZIP);
    String file1Path = commandLine.getOptionValue(FILE1);
    String file2Path = commandLine.getOptionValue(FILE2);
    if (simDocPath == null && file1Path == null) {
      showHelp();
      throw new CommandLineException("\nERROR: an input file name is invalid\n");
    } else {
      doSetInputFiles(simDocPath, file1Path, file2Path);
    }
  }

  private void doSetInputFiles(String simDocPath, String file1Path, String file2Path) {
    boolean error;
    if (simDocPath != null) {
      simDocJsonGz = new File(simDocPath);
      error = !simDocJsonGz.exists();
    } else {
      file1 = new File(file1Path);
      file2 = new File(file2Path == null ? file1Path : file2Path);
      error = !file1.isDirectory() || !file2.isDirectory();
    }
    if (error) {
      showHelp();
      throw new CommandLineException("\nERROR: an input file is not accessible\n");
    } else if (file2Path == null) {
      log.info("Assumes file2 = file1");
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");
    Option file1 = OptionBuilder.withArgName("file path").hasArg().withDescription("first file (must be a directory)").create(FILE1);
    Option file2 = OptionBuilder.withArgName("file path").hasArg().withDescription("second file (must be a directory), not mandatory (we will assume file2 = file1)").create(FILE2);
    Option simDoc = OptionBuilder.withArgName("file path").hasArg().withDescription("SimDoc model json gzip file").create(SIMDOC_JSON_GZIP);
    options.addOption(help);
    options.addOption(file1);
    options.addOption(file2);
    options.addOption(simDoc);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  private void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("ncd ", options);
  }
}
