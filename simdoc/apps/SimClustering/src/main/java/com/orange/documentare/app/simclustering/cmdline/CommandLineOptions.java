package com.orange.documentare.app.simclustering.cmdline;
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
import org.apache.commons.cli.*;

public class CommandLineOptions {
  private static final String HELP = "h";
  private static final String DISTANCES_FILE = "json";
  private static final String SIMDOC_MODE = "simdoc";
  private static final String ACUT = "acut";
  private static final String QCUT = "qcut";
  private static final String WONDER_CUT = "wcut";
  private static final String SCUT = "scut";
  private static final String CCUT = "ccut";

  private static final String A_DEFAULT_SD_FACTOR = "2";
  private static final String Q_DEFAULT_SD_FACTOR = "2";
  private static final String SCUT_DEFAULT_SD_FACTOR = "2";
  private static final String CCUT_DEFAULT_PERCENTILE = "75";

  private static final Options options = new Options();

  private SimClusteringOptionsBuilder optionsBuilder = new SimClusteringOptionsBuilder();

  public CommandLineOptions(String[] args) throws ParseException {
    init(args);
  }

  public SimClusteringOptions simClusteringOptions() {
    return optionsBuilder.build();
  }

  private void init(String[] args) throws ParseException {
    CommandLine commandLine = getCommandLineFromArgs(args);
    boolean helpRequested = commandLine.hasOption(HELP);
    if (helpRequested) {
      throw new CommandLineException("\nPrint this help message\n");
    } else {
      initOptions(commandLine);
    }
  }

  private void initOptions(CommandLine commandLine) {
    if (commandLine.hasOption(QCUT)) {
      optionsBuilder.qcut(commandLine.getOptionValue(QCUT, Q_DEFAULT_SD_FACTOR));
    }
    if (commandLine.hasOption(ACUT)) {
      optionsBuilder.acut(commandLine.getOptionValue(ACUT, A_DEFAULT_SD_FACTOR));
    }
    if (commandLine.hasOption(SCUT)) {
      optionsBuilder.scut(commandLine.getOptionValue(SCUT, SCUT_DEFAULT_SD_FACTOR));
    }
    if (commandLine.hasOption(CCUT)) {
      optionsBuilder.ccut(commandLine.getOptionValue(CCUT, CCUT_DEFAULT_PERCENTILE));
    }
    if (commandLine.hasOption(WONDER_CUT)) {
      optionsBuilder.wcut();
    }
    if (commandLine.hasOption(SIMDOC_MODE)) {
      optionsBuilder.simdocFile(commandLine.getOptionValue(SIMDOC_MODE));
    }
    if (commandLine.hasOption(DISTANCES_FILE)) {
      optionsBuilder.regularFile(commandLine.getOptionValue(DISTANCES_FILE));
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");

    Option distanceJsonOpt = Option.builder()
            .desc("distances file (.json.gz)")
            .argName(DISTANCES_FILE)
            .hasArg()
            .build();

    Option simDocOpt = Option.builder()
            .desc("path to json gzip file containing SimDoc model ready for clustering")
            .argName(SIMDOC_MODE)
            .build();

    Option qOpt = Option.builder()
            .optionalArg(true)
            .desc("graph equilaterality scissor, optional argument: standard deviation factor, default=" + Q_DEFAULT_SD_FACTOR)
            .argName(QCUT)
            .build();

    Option areaOpt = Option.builder()
            .optionalArg(true)
            .desc("graph area scissor, optional argument: standard deviation factor, default=" + A_DEFAULT_SD_FACTOR)
            .argName(ACUT)
            .build();

    Option sSdOpt = Option.builder()
            .optionalArg(true)
            .desc("subgraph scalpel, optional argument: standard deviation factor, default=" + SCUT_DEFAULT_SD_FACTOR)
            .argName(SCUT)
            .build();

    Option cTileOpt = Option.builder()
            .optionalArg(true)
            .desc("cluster scalpel, optional argument: percentile threshold, default=" + CCUT_DEFAULT_PERCENTILE)
            .argName(CCUT)
            .build();

    Option subGraphsWonderCutPost = new Option(WONDER_CUT, "enable subgraphs wonder cut post treatments");

    options.addOption(help);
    options.addOption(distanceJsonOpt);
    options.addOption(simDocOpt);
    options.addOption(qOpt);
    options.addOption(areaOpt);
    options.addOption(sSdOpt);
    options.addOption(cTileOpt);
    options.addOption(subGraphsWonderCutPost);
    CommandLineParser parser = new DefaultParser();
    return parser.parse(options, args);
  }

  public static void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("SimClustering ", options);
  }
}
