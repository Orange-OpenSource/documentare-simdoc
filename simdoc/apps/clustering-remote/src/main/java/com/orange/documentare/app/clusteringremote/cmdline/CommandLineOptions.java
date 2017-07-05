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

import com.orange.documentare.app.clusteringremote.BytesDataArray;
import com.orange.documentare.app.clusteringremote.ClusteringRequest;
import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.system.CommandLineException;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;

import static com.orange.documentare.core.comp.clustering.graph.ClusteringParameters.*;

public class CommandLineOptions {
  private static final String HELP = "h";
  private static final String INPUT_DIRECTORY = "din";
  private static final String OUTPUT_DIRECTORY = "dout";
  private static final String BYTE_DATA_JSON = "json";

  private static final String ACUT = "acut";
  private static final String QCUT = "qcut";
  private static final String WONDER_CUT = "wcut";
  private static final String SCUT = "scut";
  private static final String SLOOP = "sloop";
  private static final String CCUT = "ccut";
  private static final String KNN = "knn";

  private static final Options options = new Options();

  private ClusteringRequest.ClusteringRequestBuilder builder = ClusteringRequest.builder();

  public CommandLineOptions(String[] args) throws ParseException, IOException {
    init(args);
  }

  public ClusteringRequest clusteringRequest() {
    return builder.build();
  }

  private void init(String[] args) throws ParseException, IOException {
    CommandLine commandLine = getCommandLineFromArgs(args);
    boolean helpRequested = commandLine.hasOption(HELP);
    if (helpRequested) {
      throw new CommandLineException("\nPrint this help message\n");
    } else {
      initOptions(commandLine);
    }
  }

  private void initOptions(CommandLine commandLine) throws IOException {
    boolean dInOption = commandLine.hasOption(INPUT_DIRECTORY);
    boolean dOutOption = commandLine.hasOption(OUTPUT_DIRECTORY);
    boolean bytesDataOption = commandLine.hasOption(BYTE_DATA_JSON);

    if (!dInOption && !bytesDataOption && !dOutOption) {
      throw new CommandLineException("input directory or bytes data json or output directory argument is missing\n");
    } else {
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
      if (commandLine.hasOption(KNN)) {
        builder.kNearestNeighboursThreshold(Integer.parseInt(commandLine.getOptionValue(KNN)));
      }

      if (dInOption) {
        builder.inputDirectory(commandLine.getOptionValue(INPUT_DIRECTORY));
      }

      if (bytesDataOption) {
        String filePath = commandLine.getOptionValue(BYTE_DATA_JSON);
        builder.bytesData(loadBytesDataJson(filePath));
      }

      if (dOutOption) {
        builder.outputDirectory(commandLine.getOptionValue(OUTPUT_DIRECTORY));
      }

      builder.debug();
    }
  }

  private BytesData[] loadBytesDataJson(String filePath) throws IOException {
    JsonGenericHandler jsonGenericHandler = new JsonGenericHandler();
    return ((BytesDataArray) jsonGenericHandler.getObjectFromJsonFile(BytesDataArray.class, new File(filePath))).bytesData;
  }

  private CommandLine getCommandLineFromArgs(String[] args) throws ParseException {
    Option help = new Option(HELP, "print this message");

    Option din = OptionBuilder.withArgName("input directory").hasArg().withDescription("input directory").create(INPUT_DIRECTORY);
    Option bytesDataJson = OptionBuilder.withArgName("bytes data json").hasArg().withDescription("bytes data json").create(BYTE_DATA_JSON);
    Option dout = OptionBuilder.withArgName("output directory").hasArg().withDescription("output directory").create(OUTPUT_DIRECTORY);

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

    Option kNearestNeighboursThreshold  = Option.builder(KNN)
              .optionalArg(true)
              .hasArgs()
              .desc("kNearestNeighboursThreshold")
              .build();

    options.addOption(help);
    options.addOption(din);
    options.addOption(bytesDataJson);
    options.addOption(dout);
    options.addOption(sloop);
    options.addOption(qOpt);
    options.addOption(areaOpt);
    options.addOption(sSdOpt);
    options.addOption(cTileOpt);
    options.addOption(subGraphsWonderCutPost);
    options.addOption(kNearestNeighboursThreshold);
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
