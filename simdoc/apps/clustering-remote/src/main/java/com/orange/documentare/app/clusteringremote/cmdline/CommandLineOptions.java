package com.orange.documentare.app.clusteringremote.cmdline;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.core.system.CommandLineException;
import org.apache.commons.cli.*;

import static com.orange.documentare.core.comp.clustering.graph.ClusteringParameters.*;

public class CommandLineOptions {
  private static final String HELP = "h";
  private static final String INPUT_DIRECTORY = "d1";
  private static final String OUTPUT_DIRECTORY = "d2";

  private static final String ACUT = "acut";
  private static final String QCUT = "qcut";
  private static final String WONDER_CUT = "wcut";
  private static final String SCUT = "scut";
  private static final String SLOOP = "sloop";
  private static final String CCUT = "ccut";

  private static final Options options = new Options();

  private ClusteringRemoteOptions.SimClusteringOptionsBuilder optionsBuilder = ClusteringRemoteOptions.builder();

  public CommandLineOptions(String[] args) throws ParseException {
    init(args);
  }

  public ClusteringRemoteOptions simClusteringOptions() {
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
    boolean d1Option = commandLine.hasOption(INPUT_DIRECTORY);

    if (!d1Option) {
      throw new CommandLineException("\nERROR: an input file argument is missing\n");
    } else {
      ClusteringParameters.ClusteringParametersBuilder builder = optionsBuilder.clusteringParametersBuilder;

      if (commandLine.hasOption(ACUT)) {
        builder.acut(Float.parseFloat(commandLine.getOptionValue(ACUT, String.valueOf(A_DEFAULT_SD_FACTOR))));
      }
      if (commandLine.hasOption(QCUT)) {
        builder.qcut(Float.parseFloat(commandLine.getOptionValue(QCUT, String.valueOf(Q_DEFAULT_SD_FACTOR))));
      }
      if (commandLine.hasOption(SCUT)) {
        builder.scut(Float.parseFloat(commandLine.getOptionValue(SCUT, String.valueOf(SCUT_DEFAULT_SD_FACTOR))));
      }
      if (commandLine.hasOption(SLOOP)) {
        builder.sloop();
      }
      if (commandLine.hasOption(CCUT)) {
        builder.ccut(Integer.parseInt(commandLine.getOptionValue(CCUT, String.valueOf(CCUT_DEFAULT_PERCENTILE))));
      }
      if (commandLine.hasOption(WONDER_CUT)) {
        builder.wcut();
      }
    }
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");

    Option d1 = OptionBuilder.withArgName("directory path").hasArg().withDescription("directory d1").create(INPUT_DIRECTORY);
    Option d2 = OptionBuilder.withArgName("directory path").hasArg().withDescription("directory d2, not mandatory (we assume d2=d1)").create(OUTPUT_DIRECTORY);

    Option sloop = new Option(SLOOP, "subgraph scalpel, adaptative mode");


    Option areaOpt = Option.builder(ACUT)
            .optionalArg(true)
            .hasArgs()
            .desc("graph area scissor, optional argument: standard deviation factor, default=" + A_DEFAULT_SD_FACTOR)
            .build();

    Option qOpt = Option.builder(QCUT)
            .optionalArg(true)
            .hasArgs()
            .desc("graph equilaterality scissor, optional argument: standard deviation factor, default=" + Q_DEFAULT_SD_FACTOR)
            .build();


    Option sSdOpt = Option.builder(SCUT)
            .optionalArg(true)
            .hasArgs()
            .desc("subgraph scalpel, optional argument: standard deviation factor, default=" + SCUT_DEFAULT_SD_FACTOR)
            .build();

    Option cTileOpt = Option.builder(CCUT)
            .optionalArg(true)
            .hasArgs()
            .desc("cluster scalpel, optional argument: percentile threshold, default=" + CCUT_DEFAULT_PERCENTILE)
            .build();

    Option subGraphsWonderCutPost = new Option(WONDER_CUT, "enable subgraphs wonder cut post treatments");

    options.addOption(help);
    options.addOption(d1);
    options.addOption(d2);
    options.addOption(sloop);
    options.addOption(qOpt);
    options.addOption(areaOpt);
    options.addOption(sSdOpt);
    options.addOption(cTileOpt);
    options.addOption(subGraphsWonderCutPost);
    CommandLineParser parser = new DefaultParser();
    return parser.parse(options, args);
  }

  public static void showHelp(Exception e) {
    System.out.println();
    System.out.println("[ERROR] " + e.getMessage());
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("ClusteringRemote ", options);
  }
}
