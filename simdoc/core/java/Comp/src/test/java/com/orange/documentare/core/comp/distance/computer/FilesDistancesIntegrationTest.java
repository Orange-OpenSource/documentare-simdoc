package com.orange.documentare.core.comp.distance.computer;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.distance.filesdistances.FilesDistances;
import com.orange.documentare.core.comp.distance.matrix.DistancesMatrixCsvGzipWriter;
import com.orange.documentare.core.comp.measure.ProgressListener;
import com.orange.documentare.core.comp.measure.TreatmentStep;
import com.orange.documentare.core.model.io.Gzip;
import com.orange.documentare.core.system.measure.Progress;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

public class FilesDistancesIntegrationTest {

  private static final String TEST_DIRECTORY_A = "/bestioles";
  private static final String TEST_DIRECTORY_B = "/comp_dir";
  private static final String TEST_REFERENCE_AA = "/bestioles.reference.gz";
  private static final String TEST_REFERENCE_AB = "/a2b_matrix.reference.gz";
  private static final String TEST_REFERENCE_BA = "/b2a_matrix.reference.gz";
  private static final String TEST_REFERENCE_AB_NEARESTS = "/a2b_nearests.reference.gz";
  private static final String TEST_REFERENCE_BA_NEARESTS = "/b2a_nearests.reference.gz";

  private static final File TEST_MATRIX_OUTPUT = new File("matrix.csv.gz");
  private static final File TEST_NEARESTS_OUTPUT = new File("nearests.csv.gz");

  private static final File TEST_MATRIX_OUTPUT_SAME_ARRAY = new File("matrix_same_array.csv.gz");
  private static final File TEST_NEARESTS_OUTPUT_SAME_ARRAY = new File("nearests_same_array.csv.gz");


  @After
  public void cleanup() {
    FileUtils.deleteQuietly(TEST_MATRIX_OUTPUT);
    FileUtils.deleteQuietly(TEST_NEARESTS_OUTPUT);
    FileUtils.deleteQuietly(TEST_MATRIX_OUTPUT_SAME_ARRAY);
    FileUtils.deleteQuietly(TEST_NEARESTS_OUTPUT_SAME_ARRAY);
  }

  @Test
  public void shouldComputeSelfMatrix() throws IOException {
    shouldCompute(TEST_DIRECTORY_A, TEST_DIRECTORY_A, TEST_REFERENCE_AA, false);
  }

  @Test
  public void shouldComputeA2B() throws IOException {
    shouldCompute(TEST_DIRECTORY_A, TEST_DIRECTORY_B, TEST_REFERENCE_AB, false);
  }

  @Test
  public void shouldComputeB2A() throws IOException {
    shouldCompute(TEST_DIRECTORY_B, TEST_DIRECTORY_A, TEST_REFERENCE_BA, false);
  }

  @Test
  public void shouldComputeA2BNearests() throws IOException {
    shouldCompute(TEST_DIRECTORY_A, TEST_DIRECTORY_B, TEST_REFERENCE_AB_NEARESTS, true);
  }

  @Test
  public void shouldComputeB2ANearests() throws IOException {
    shouldCompute(TEST_DIRECTORY_B, TEST_DIRECTORY_A, TEST_REFERENCE_BA_NEARESTS, true);
  }

  void shouldCompute(String dir1, String dir2, String ref, boolean nearestsMode) throws IOException {
    // given
    FilesDistances filesDistances = FilesDistances.empty();
    boolean sameArray = dir1.equals(dir2);
    File directory1 = new File(getClass().getResource(dir1).getFile());
    File directory2 = new File(getClass().getResource(dir2).getFile());
    String refFileResource = Gzip.getStringFromGzipFile(new File(getClass().getResource(ref).getFile()));

    // do
    FilesDistances fd = filesDistances.compute(directory1, directory2, null);
    DistancesMatrixCsvGzipWriter writer = new DistancesMatrixCsvGzipWriter(fd.items1, sameArray ? fd.items1 : fd.items2, fd.distancesArray);
    writer.writeTo(nearestsMode ? TEST_NEARESTS_OUTPUT : TEST_MATRIX_OUTPUT, nearestsMode);
    String out = Gzip.getStringFromGzipFile(nearestsMode ? TEST_NEARESTS_OUTPUT : TEST_MATRIX_OUTPUT);

    // then
    Assert.assertEquals(refFileResource, out);
  }
}
