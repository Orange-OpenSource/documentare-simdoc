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
import lombok.Getter;
import org.apache.commons.cli.*;

import java.io.File;

@Getter
public class CommandLineOptions {
  private static final String HELP = "h";
  private static final String DISTANCES_FILE = "json";
  private static final String SIMDOC_MODE = "simdoc";
  private static final String Q_SD_FACTOR = "sdq";
  private static final String AREA_SD_FACTOR = "sdarea";
  private static final String SUBGRAPH_SCALPEL_SD = "sdscut";
  private static final String CLUSTER_SCALPEL_PERCENTILE = "tileccut";
  private static final String WONDER_CUT = "wcut";
  private static final String SUBGRAPH_CUT = "scut";
  private static final String CLUSTER_CUT = "ccut";

  private static final Options options = new Options();

  private File inputJsonGz;
  private File simDocJson;
  private float qStdFactor = -1;
  private float areaSdFactor = -1;
  private float subgraphScalpelSdFactor = -1;
  private int clusterDistThreshPercentile = -1;
  private boolean subGraphsWonderCutEnabled;
  private boolean subGraphsScalpelCutEnabled;
  private boolean clustersScalpelCutEnabled;

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
    checkInputFiles(commandLine);
  }

  private void checkInputFiles(CommandLine commandLine) {
    if (!commandLine.hasOption(SIMDOC_MODE) && !commandLine.hasOption(DISTANCES_FILE)) {
      showHelp();
      throw new CommandLineException("\nERROR: an input file argument is missing\n");
    } else {
      setGraphConfiguration(commandLine);
      setInputFiles(commandLine);
    }
  }

  private void setGraphConfiguration(CommandLine commandLine) {
    if (commandLine.hasOption(Q_SD_FACTOR)) {
      qStdFactor = Float.parseFloat(commandLine.getOptionValue(Q_SD_FACTOR));
    }
    if (commandLine.hasOption(AREA_SD_FACTOR)) {
      areaSdFactor = Float.parseFloat(commandLine.getOptionValue(AREA_SD_FACTOR));
    }
    if (commandLine.hasOption(SUBGRAPH_SCALPEL_SD)) {
      subgraphScalpelSdFactor = Float.parseFloat(commandLine.getOptionValue(SUBGRAPH_SCALPEL_SD));
    }
    if (commandLine.hasOption(CLUSTER_SCALPEL_PERCENTILE)) {
      clusterDistThreshPercentile = Integer.parseInt(commandLine.getOptionValue(CLUSTER_SCALPEL_PERCENTILE));
    }
    if (commandLine.hasOption(WONDER_CUT)) {
      subGraphsWonderCutEnabled = true;
    } else if (commandLine.hasOption(SUBGRAPH_CUT)) {
      subGraphsScalpelCutEnabled = true;
    }
    if (commandLine.hasOption(CLUSTER_CUT)) {
      clustersScalpelCutEnabled = true;
    }
  }

  private void setInputFiles(CommandLine commandLine) {
    String simDocPath = commandLine.getOptionValue(SIMDOC_MODE);
    String distancesJsonPath = commandLine.getOptionValue(DISTANCES_FILE);
    if (distancesJsonPath == null && simDocPath == null) {
      showHelp();
      throw new CommandLineException("\nERROR: an input argument is invalid\n");
    } else {
      doSetInputFiles(distancesJsonPath, simDocPath);
    }
  }

  private void doSetInputFiles(String distancesJsonPath, String simDocPath) {
    boolean error;
    if (distancesJsonPath != null) {
      inputJsonGz = new File(distancesJsonPath);
      error = !inputJsonGz.isFile();
    } else {
      simDocJson = new File(simDocPath);
      error = !simDocJson.exists();
    }
    if (error) {
      showHelp();
      throw new CommandLineException("\nERROR: an input file is not accessible\n");
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");
    Option distanceJsonOpt = OptionBuilder.withArgName("distances json gzip file path").hasArg().withDescription("path to json gzip file containing items distances").create(DISTANCES_FILE);
    Option simDocOpt = OptionBuilder.withArgName("SimDoc json gzip file path").hasArg().withDescription("path to json gzip file containing SimDoc model ready for clustering").create(SIMDOC_MODE);

    Option qOpt = OptionBuilder.withArgName("graph scissor Q SD factor").hasArg().withDescription("graph scissor equilaterality's standard deviation factor").create(Q_SD_FACTOR);
    Option areaOpt = OptionBuilder.withArgName("graph scissor Area SD factor").hasArg().withDescription("graph scissor area's standard deviation factor").create(AREA_SD_FACTOR);

    Option sSdOpt = OptionBuilder.withArgName("subgraph scalpel SD factor").hasArg().withDescription("subgraph scalpel standard deviation factor").create(SUBGRAPH_SCALPEL_SD);
    Option cTileOpt = OptionBuilder.withArgName("cluster scalpel percentile threshold").hasArg().withDescription("cluster scalpel percentile threshold").create(CLUSTER_SCALPEL_PERCENTILE);

    Option subGraphsScalpelCutPost = new Option(SUBGRAPH_CUT, "enable subgraphs scalpel cut post treatments");
    Option clustersScalpelCutPost = new Option(CLUSTER_CUT, "enable clusters scalpel cut post treatments");
    Option subGraphsWonderCutPost = new Option(WONDER_CUT, "enable subgraphs wonder cut post treatments");
    options.addOption(help);
    options.addOption(distanceJsonOpt);
    options.addOption(simDocOpt);
    options.addOption(qOpt);
    options.addOption(areaOpt);
    options.addOption(sSdOpt);
    options.addOption(cTileOpt);
    options.addOption(subGraphsScalpelCutPost);
    options.addOption(clustersScalpelCutPost);
    options.addOption(subGraphsWonderCutPost);
    CommandLineParser parser = new PosixParser();
    return parser.parse(options, args);
  }

  public static void showHelp() {
    System.out.println();
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("sim clustering ", options);
  }
}
