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

import com.orange.documentare.app.clusteringremote.cmdline.CommandLineOptions;
import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.core.system.CommandLineException;
import org.apache.commons.cli.ParseException;
import org.fest.assertions.Assertions;
import org.junit.Test;

import static com.orange.documentare.core.comp.clustering.graph.ClusteringParameters.*;

public class CommandLineOptionsTest {

  @Test(expected = CommandLineException.class)
  public void missing_input_files_raised_exception() throws ParseException {
    // Given
    String[] args = {};
    // When
    (new CommandLineOptions(args)).simClusteringOptions();
    // Then
   }

  @Test
  public void enable_acut_qcut_scut_ccut_wcut_with_defaults() throws ParseException {
    // Given
    String[] args = {
            "-acut", "-qcut", "-scut", "-ccut", "-wcut", "-simdoc", "."
    };

    // When
    ClusteringParameters options = (new CommandLineOptions(args)).simClusteringOptions().clusteringParameters;

    // Then
    Assertions.assertThat(options.acut()).isTrue();
    Assertions.assertThat(options.qcut()).isTrue();
    Assertions.assertThat(options.scut()).isTrue();
    Assertions.assertThat(options.ccut()).isTrue();
    Assertions.assertThat(options.wcut).isTrue();
    Assertions.assertThat(options.knn()).isFalse();

    Assertions.assertThat(options.acutSdFactor).isEqualTo(A_DEFAULT_SD_FACTOR);
    Assertions.assertThat(options.qcutSdFactor).isEqualTo(Q_DEFAULT_SD_FACTOR);
    Assertions.assertThat(options.scutSdFactor).isEqualTo(SCUT_DEFAULT_SD_FACTOR);
    Assertions.assertThat(options.ccutPercentile).isEqualTo(CCUT_DEFAULT_PERCENTILE);
  }

  @Test
  public void enable_acut_qcut_scut_ccut() throws ParseException {
    // Given
    String[] args = {
            "-acut", "0.1", "-qcut", "0.2", "-scut", "0.3", "-ccut", "23",
            "-simdoc", "."
    };

    // When
    ClusteringParameters options = (new CommandLineOptions(args)).simClusteringOptions().clusteringParameters;

    // Then
    Assertions.assertThat(options.acut()).isTrue();
    Assertions.assertThat(options.qcut()).isTrue();
    Assertions.assertThat(options.scut()).isTrue();
    Assertions.assertThat(options.ccut()).isTrue();
    Assertions.assertThat(options.wcut).isFalse();
    Assertions.assertThat(options.knn()).isFalse();

    Assertions.assertThat(options.acutSdFactor).isEqualTo(0.1f);
    Assertions.assertThat(options.qcutSdFactor).isEqualTo(0.2f);
    Assertions.assertThat(options.scutSdFactor).isEqualTo(0.3f);
    Assertions.assertThat(options.ccutPercentile).isEqualTo(23);
  }
}