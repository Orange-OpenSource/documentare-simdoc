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
import com.orange.documentare.core.image.opencv.OpencvLoader;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Log4j2
public class ClusteringTasksServiceTest {

  private static final int NB_TASKS = 4 * 20;
  private static final String CLUSTERING_TASK_FILE_PREFIX = "clustering_tasks_";

  private final ClusteringTasksService tasksHandler = ClusteringTasksService.instance();
  private final ClusteringParameters parameters = ClusteringParameters.builder().acut().qcut().build();

  @Test
  public void runSeveralDistinctTasks() throws IOException, InterruptedException {
    // given
    String refJson1 = FileUtils.readFileToString(new File(getClass().getResource("/clusteringtasks/latin1_clustering.ref.json").getFile()));
    String refJson2 = FileUtils.readFileToString(new File(getClass().getResource("/clusteringtasks/latin2_clustering.ref.json").getFile()));
    String refJson3 = FileUtils.readFileToString(new File(getClass().getResource("/clusteringtasks/latin3_clustering.ref.json").getFile()));
    String refJson4 = FileUtils.readFileToString(new File(getClass().getResource("/clusteringtasks/latin4_clustering.ref.json").getFile()));

    File segFile1 = new File(getClass().getResource("/clusteringtasks/latin1_segmentation.json").getFile());
    File segFile2 = new File(getClass().getResource("/clusteringtasks/latin2_segmentation.json").getFile());
    File segFile3 = new File(getClass().getResource("/clusteringtasks/latin3_segmentation.json").getFile());
    File segFile4 = new File(getClass().getResource("/clusteringtasks/latin4_segmentation.json").getFile());

    String[] outputFilenames = new String[NB_TASKS];
    ClusteringTask[] clusteringTasks = new ClusteringTask[NB_TASKS];
    for (int i = 0; i < NB_TASKS; i++) {
      outputFilenames[i] = CLUSTERING_TASK_FILE_PREFIX + i + ".json";
    }
    for (int i = 0; i < NB_TASKS/4; i++) {
      clusteringTasks[i * 4]     = new ClusteringTask(segFile1.getAbsolutePath(), outputFilenames[i * 4], parameters);
      clusteringTasks[i * 4 + 1] = new ClusteringTask(segFile2.getAbsolutePath(), outputFilenames[i * 4 + 1], parameters);
      clusteringTasks[i * 4 + 2] = new ClusteringTask(segFile3.getAbsolutePath(), outputFilenames[i * 4 + 2], parameters);
      clusteringTasks[i * 4 + 3] = new ClusteringTask(segFile4.getAbsolutePath(), outputFilenames[i * 4 + 3], parameters);
    }

    // when
    for (int i = 0; i < NB_TASKS; i++) {
      tasksHandler.addNewTask(clusteringTasks[i]);
      Thread.sleep(200);
      log.info(tasksHandler.tasksDescription());
    }
    tasksHandler.waitForAllTasksDone();

    String[] outputJsons = new String[NB_TASKS];
    for (int i = 0; i < NB_TASKS; i++) {
      outputJsons[i] = FileUtils.readFileToString(new File(outputFilenames[i]));
    }

    // then
    for (int i = 0; i < NB_TASKS/4; i++) {
      Assertions.assertThat(outputJsons[i * 4]).isEqualTo(refJson1);
      Assertions.assertThat(outputJsons[i * 4 + 1]).isEqualTo(refJson2);
      Assertions.assertThat(outputJsons[i * 4 + 2]).isEqualTo(refJson3);
      Assertions.assertThat(outputJsons[i * 4 + 3]).isEqualTo(refJson4);
    }

    Arrays.stream(outputFilenames)
            .forEach(f -> FileUtils.deleteQuietly(new File(f)));
  }

  @Test
  public void saveGraph() throws IOException, InterruptedException {
    // given
    OpencvLoader.load();

    String filename = "my_graph";
    File myGraphPdf = new File(filename + ".pdf");
    myGraphPdf.delete();

    File segFile = new File(getClass().getResource("/clusteringtasks/latin1_segmentation.json").getFile());
    String outputFilename = "clustering_task_write_graph.json";
    ClusteringTask clusteringTask = new ClusteringTask(segFile.getAbsolutePath(), outputFilename, parameters);
    clusteringTask.saveGraphTo(filename);

    // when
    tasksHandler.addNewTask(clusteringTask);
    tasksHandler.waitForAllTasksDone();

    // then
    Assertions.assertThat(myGraphPdf).exists();
    Assertions.assertThat(myGraphPdf.length()).isGreaterThan(10000);
  }

  @Test
  public void saveStrippedOutput() throws IOException, InterruptedException {
    // given
    OpencvLoader.load();

    File ref = new File(getClass().getResource("/clusteringtasks/stripped_clustering.ref.json").getFile());
    String jsonRef = FileUtils.readFileToString(ref);

    String strippedFilename = "stripped_clustering.json";
    File strippedFile = new File(strippedFilename);
    strippedFile.delete();

    File segFile = new File(getClass().getResource("/clusteringtasks/latin1_segmentation.json").getFile());
    String outputFilename = "not_stripped_clustering.json";
    ClusteringTask task = new ClusteringTask(segFile.getAbsolutePath(), outputFilename, parameters);
    task.strippedOutputFilename(strippedFilename);


    // when
    tasksHandler.addNewTask(task);
    tasksHandler.waitForAllTasksDone();

    // then
    String strippedJson = FileUtils.readFileToString(strippedFile);
    Assertions.assertThat(strippedFile).exists();
    Assertions.assertThat(strippedJson).isEqualTo(jsonRef);
  }
}
