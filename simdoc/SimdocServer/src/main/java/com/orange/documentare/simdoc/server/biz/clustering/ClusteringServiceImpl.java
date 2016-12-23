package com.orange.documentare.simdoc.server.biz.clustering;

import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.core.system.filesid.FilesIdBuilder;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ClusteringServiceImpl implements ClusteringService {

  private static final String SAFE_INPUT_DIR = "/safe-input-dir";

  @Override
  public ClusteringResult build(
    File inputDirectory, File outputDirectory, ClusteringParameters parameters, boolean debug) {

    String outputDirectoryAbsPath = outputDirectory.getAbsolutePath();
    String safeInputDirAbsPath = outputDirectoryAbsPath + SAFE_INPUT_DIR;

    // Create safe input dir
    FilesIdBuilder filesIdBuilder = new FilesIdBuilder();
    filesIdBuilder.createFilesIdDirectory(
      inputDirectory.getAbsolutePath(),
      safeInputDirAbsPath,
      outputDirectoryAbsPath);

    // Ncd



    return null;
  }
}
