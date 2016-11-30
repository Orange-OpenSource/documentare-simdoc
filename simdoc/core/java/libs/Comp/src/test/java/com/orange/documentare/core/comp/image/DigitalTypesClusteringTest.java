package com.orange.documentare.core.comp.image;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.core.comp.measure.Progress;
import com.orange.documentare.core.comp.measure.ProgressListener;
import com.orange.documentare.core.comp.measure.TreatmentStep;
import com.orange.documentare.core.model.io.Gzip;
import com.orange.documentare.core.model.json.JsonGenericHandler;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import com.orange.documentare.core.model.ref.segmentation.ImageSegmentation;
import org.fest.assertions.Assertions;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

@RunWith(ZohhakRunner.class)
public class DigitalTypesClusteringTest implements ProgressListener {

  private final JsonGenericHandler jsonGenericHandler = new JsonGenericHandler(true);

  @TestWith({
          "/latin_segmentation.reference.json.gz, /latin_segmentation_with_cluster_id.reference.json.gz, latin_segmentation_distances_built_with_cluster_id.json.gz, false, false",
          "/latin_segmentation_with_distances.reference.json.gz, /latin_segmentation_with_cluster_id_and_distances.reference.json.gz, latin_segmentation_with_cluster_id.json.gz, true, true"
  })
  public void updateDigitalTypesClusterId(String segmentationInput, String withCLusterIdRef, String withClusterIdOutput, boolean distancesAvailable, boolean keepDistances) throws IOException {
    // given
    ImageSegmentation imageSegmentation = loadSegmentationTest(segmentationInput);
    DigitalTypes digitalTypes = imageSegmentation.getDigitalTypes();
    DigitalTypesClustering digitalTypesClustering = new DigitalTypesClustering();
    digitalTypesClustering.setProgressListener(this);
    digitalTypesClustering.setComputeDistances(!distancesAvailable);
    digitalTypesClustering.setClearDistances(!keepDistances);
    ClusteringParameters parameters = clusteringParameters();
    String outputReference = loadReference(withCLusterIdRef);

    // when
    digitalTypesClustering.computeClusterIdsOf(digitalTypes, parameters);
    imageSegmentation.setDistancesAvailable(keepDistances);
    imageSegmentation.setClustersAvailable(true);

    String outputReloaded = saveAndReloadOutput(imageSegmentation, withClusterIdOutput);

    // then
    Assertions.assertThat(outputReloaded).isEqualTo(outputReference);
  }

  /** change default settings so that we check we actually test it as well */
  private ClusteringParameters clusteringParameters() {
    ClusteringParameters parameters = new ClusteringParameters();
    parameters.setStdAreaFactor(4);
    parameters.setStdQFactor(4);
    parameters.setCutSubgraphLongestVerticesEnabled(true);
    parameters.setStdSubgraphDistanceFactor(4);
    parameters.setCutClusterLongestVerticesEnabled(true);
    parameters.setDistClusterThreshPercentile(90);
    return parameters;
  }

  private ImageSegmentation loadSegmentationTest(String segmentationInput) throws IOException {
    File segmentationInputFile = new File(getClass().getResource(segmentationInput).getFile());
    return (ImageSegmentation) jsonGenericHandler.getObjectFromJsonGzipFile(ImageSegmentation.class, segmentationInputFile);
  }

  private String loadReference(String refName) throws IOException {
    File refFile = new File(getClass().getResource(refName).getFile());
    return Gzip.getStringFromGzipFile(refFile);
  }

  private String saveAndReloadOutput(Object object, String outputName) throws IOException {
    File outputFile = new File(outputName);
    jsonGenericHandler.writeObjectToJsonGzipFile(object, outputFile);
    return Gzip.getStringFromGzipFile(outputFile);
  }

  @Override
  public void onProgressUpdate(TreatmentStep step, Progress progress) {
    System.out.println(progress.displayString(step.toString()));
  }
}
