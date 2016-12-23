package com.orange.documentare.simdoc.server.biz.clustering;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.documentare.core.comp.clustering.graph.ClusteringGraphBuilder;
import com.orange.documentare.core.comp.clustering.graph.ClusteringParameters;
import com.orange.documentare.core.comp.distance.DistancesArray;
import com.orange.documentare.core.comp.distance.filesdistances.FilesDistances;
import com.orange.documentare.core.model.ref.clustering.ClusteringItem;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.comp.DistanceItem;
import com.orange.documentare.core.model.ref.comp.NearestItem;
import com.orange.documentare.core.model.ref.comp.TriangleVertices;
import com.orange.documentare.core.system.filesid.FilesIdBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ClusteringServiceImpl implements ClusteringService {

  private static final String SAFE_INPUT_DIR = "/safe-input-dir";
  private static final String CLUSTERING_RESULT_FILE = "/clustering-result.json";

  // FIXME: REFACTORING NEEDED, DEBUG NOT IMPLEMENTED
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

    // Sim Clustering
    SimClusteringItem[] simClusteringItems = initClusteringItems(filesDistances, parameters);
    ClusteringGraphBuilder clusteringGraphBuilder = new ClusteringGraphBuilder();
    ClusteringGraph graph = clusteringGraphBuilder.build(simClusteringItems, parameters);

    // Prep output data
    ClusteringResultItem[] clusteringResultItems =
      ClusteringResultItem.buildItems(inputDirectory, filesIdBuilder.readMapIn(outputDirectoryAbsPath), simClusteringItems);

    // Write to output directory
    ClusteringResult clusteringResult = ClusteringResult.with(clusteringResultItems);
    write(outputDirectoryAbsPath, clusteringResult);

    return clusteringResult;
  }

  private void write(String outputDirectoryAbsPath, ClusteringResult clusteringResult) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(new File(outputDirectoryAbsPath + CLUSTERING_RESULT_FILE), clusteringResult);
  }

  private SimClusteringItem[] initClusteringItems(FilesDistances filesDistances, ClusteringParameters parameters) {
    int nbItems = filesDistances.items1.length;
    int k = parameters.knn() ? parameters.kNearestNeighboursThreshold : nbItems;
    SimClusteringItem[] simClusteringItems = new SimClusteringItem[nbItems];
    for(int i = 0; i < nbItems; i++) {
      simClusteringItems[i] = new SimClusteringItem(filesDistances.items1[i].relativeFilename);
    }
    buildTriangulationVertices(simClusteringItems, filesDistances.distancesArray, k);
    return simClusteringItems;
  }

  /** Memory in place creation, it is optimal since we do not allocate nearest arrays */
  private void buildTriangulationVertices(SimClusteringItem[] simClusteringItems, DistancesArray distancesArray, int k) {
    List<SimClusteringItem> itemsList = Arrays.asList(simClusteringItems);
    for (int i = 0; i < simClusteringItems.length; i++) {
      NearestItem vertex2 = distancesArray.nearestItemOf(i);
      NearestItem vertex3 = distancesArray.nearestItemOfBut(vertex2.getIndex(), i);
      simClusteringItems[i].setTriangleVertices(
        new TriangleVertices(distancesArray.nearestItemsFor(itemsList, i), vertex3, k));
    }
  }

  @Getter
  @Setter
  public class SimClusteringItem implements ClusteringItem, DistanceItem {
    /** Keep the relative path name, and useful for items comparison when distance are equal */
    public final String humanReadableId;
    public TriangleVertices triangleVertices;
    public Integer clusterId;
    public boolean clusterCenter;

    private SimClusteringItem(String relativeFilename) {
      // FIXME graphviz exception here: remove leading '/', graphviz hates it
      humanReadableId = relativeFilename.substring(1);
    }

    @Override
    public boolean triangleVerticesAvailable() {
      return true;
    }

    /** Not used */
    @Override
    public NearestItem[] getNearestItems() {
      return null;
    }
    @Override
    public byte[] getBytes() {
      return null;
    }
  }
}
