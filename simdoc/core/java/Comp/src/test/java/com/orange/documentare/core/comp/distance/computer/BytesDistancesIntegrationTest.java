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

import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.comp.distance.bytesdistances.BytesData;
import com.orange.documentare.core.comp.distance.bytesdistances.BytesDistances;
import com.orange.documentare.core.comp.distance.matrix.DistancesMatrixCsvGzipWriter;
import com.orange.documentare.core.model.io.Gzip;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class BytesDistancesIntegrationTest {

  private static final String TEST_DIRECTORY_A = "/bestioles";
  private static final String TEST_DIRECTORY_B = "/comp_dir";
  private static final String TEST_REFERENCE_AA = "/bestioles.reference.gz";
  private static final String TEST_REFERENCE_AB = "/a2b_matrix.reference.gz";
  private static final String TEST_REFERENCE_AB_NEARESTS = "/a2b_nearests.reference.gz";

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
  public void compute_distances_matrix_between_elements_of_a_directory() throws IOException {
    shouldCompute(TEST_DIRECTORY_A, TEST_DIRECTORY_A, TEST_REFERENCE_AA, false);
  }
  @Test
  public void compute_distances_matrix_between_two_directories() throws IOException {
    shouldCompute(TEST_DIRECTORY_A, TEST_DIRECTORY_B, TEST_REFERENCE_AB, false);
  }
  @Test
  public void compute_distances_nearest_array_between_two_directories() throws IOException {
    shouldCompute(TEST_DIRECTORY_A, TEST_DIRECTORY_B, TEST_REFERENCE_AB_NEARESTS, true);
  }

  void shouldCompute(String dir1, String dir2, String ref, boolean nearestsMode) throws IOException {
    // given
    BytesDistances bytesDistances = new BytesDistances();
    File directory1 = new File(getClass().getResource(dir1).getFile());
    File directory2 = new File(getClass().getResource(dir2).getFile());
    BytesData.FileIdProvider fileIdProvider = file -> file.getName();
    BytesData[] bytesDatas1 = BytesData.loadFromDirectory(directory1, fileIdProvider);
    BytesData[] bytesDatas2 = BytesData.loadFromDirectory(directory2, fileIdProvider);

    String refFileResource = Gzip.getStringFromGzipFile(new File(getClass().getResource(ref).getFile()));

    // do
    DistancesArray distancesArray = bytesDistances.computeDistancesBetweenCollections(bytesDatas1, bytesDatas2);
    DistancesMatrixCsvGzipWriter writer = new DistancesMatrixCsvGzipWriter(bytesDatas1, (directory1.equals(directory2)) ? bytesDatas1 : bytesDatas2, distancesArray);
    writer.writeTo(nearestsMode ? TEST_NEARESTS_OUTPUT : TEST_MATRIX_OUTPUT, nearestsMode);
    String out = Gzip.getStringFromGzipFile(nearestsMode ? TEST_NEARESTS_OUTPUT : TEST_MATRIX_OUTPUT);

    // then
    Assert.assertEquals(refFileResource, out);
  }
}
