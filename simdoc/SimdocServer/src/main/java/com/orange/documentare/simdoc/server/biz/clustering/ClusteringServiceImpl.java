package com.orange.documentare.simdoc.server.biz.clustering;

import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.core.comp.distance.filesdistances.FilesDistances;
import com.orange.documentare.core.system.filesid.FilesIdBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ClusteringServiceImpl implements ClusteringService {

  private static final String SAFE_INPUT_DIR = "/safe-input-dir";

  @Override
  public ClusteringResult build(
    File inputDirectory, File outputDirectory, ClusteringParameters parameters, boolean debug) throws IOException {

    String outputDirectoryAbsPath = outputDirectory.getAbsolutePath();
    File safeInputDir = new File(outputDirectoryAbsPath + SAFE_INPUT_DIR);

    // Create safe input dir
    FilesIdBuilder filesIdBuilder = new FilesIdBuilder();
    filesIdBuilder.createFilesIdDirectory(
      inputDirectory.getAbsolutePath(),
      safeInputDir.getAbsolutePath(),
      outputDirectoryAbsPath);

    // Compute files distances
    FilesDistances filesDistances = FilesDistances.empty();
    filesDistances = filesDistances.compute(safeInputDir, safeInputDir, null);

    // Prep data for SimClustering


    // Prep output data

    return null;
  }
}
