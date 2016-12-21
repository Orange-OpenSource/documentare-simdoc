package com.orange.documentare.core.comp.clustering.tasksservice;
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
import com.orange.documentare.core.graphwriter.GraphWriterPdf;
import com.orange.documentare.core.image.opencv.OpencvLoader;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

@Slf4j
public class GraphWriterPdfTest {

  static {
    OpencvLoader.load();
  }

  private final ClusteringTasksService tasksHandler = ClusteringTasksService.instance();
  private final ClusteringParameters parameters = ClusteringParameters.builder().acut().qcut().build();
  private final String outputFilename = "clustering_task_write_graph.json";
  private String pdfname;
  private File myGraphPdf;

  @Before
  @After
  public void cleanup() {
    pdfname = "my_graph";
    myGraphPdf = new File(pdfname + ".pdf");
    myGraphPdf.delete();
    (new File(outputFilename)).delete();
  }

  @Test
  public void saveGraph() throws IOException, InterruptedException {
    // given
    File segFile = new File(getClass().getResource("/latin1_segmentation.json").getFile());
    ClusteringTask task = ClusteringTask.builder()
      .inputFilename(segFile.getAbsolutePath())
      .outputFilename(outputFilename)
      .clusteringParameters(parameters)
      .writeGraphTo(pdfname)
      .graphWriter(new GraphWriterPdf())
      .build();

    // when
    tasksHandler.addNewTask(task);
    tasksHandler.waitForAllTasksDone();

    // then
    Assertions.assertThat(myGraphPdf).exists();
    Assertions.assertThat(myGraphPdf.length()).isGreaterThan(10000);
  }
}
