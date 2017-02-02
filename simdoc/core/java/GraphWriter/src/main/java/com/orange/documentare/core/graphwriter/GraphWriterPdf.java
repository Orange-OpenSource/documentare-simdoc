package com.orange.documentare.core.graphwriter;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.comp.clustering.graph.GraphvizPath;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.JGraphEdge;
import com.orange.documentare.core.comp.clustering.graph.jgrapht.JGraphTBuilder;
import com.orange.documentare.core.comp.clustering.tasksservice.GraphWriter;
import com.orange.documentare.core.image.opencv.OpenCvImage;
import com.orange.documentare.core.model.ref.clustering.graph.ClusteringGraph;
import com.orange.documentare.core.model.ref.clustering.graph.GraphItem;
import com.orange.documentare.core.model.ref.segmentation.DigitalType;
import com.orange.documentare.core.model.ref.segmentation.DigitalTypes;
import com.orange.documentare.core.system.nativeinterface.NativeInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.graph.AbstractBaseGraph;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class GraphWriterPdf implements GraphWriter {
  private static final String IMAGES_PREFIX = "/tmp/graph_images/";

  private final String imagesDir = IMAGES_PREFIX + UUID.randomUUID();
  private final String graphDot = imagesDir + "/graph.dot";

  @Override
  public void write(String outputGraphFilename, ClusteringGraph clusteringGraph, DigitalTypes digitalTypes) {
    try {
      writeImages(digitalTypes);
      writeGraphDot(clusteringGraph);
      buildPdf(outputGraphFilename);
    } catch (IOException e) {
      log.error("Failed to write graph to file " + outputGraphFilename + " / e = " + e.getMessage(), e);
    }
  }

  private void writeImages(DigitalTypes digitalTypes) throws IOException {
    for (DigitalType digitalType : digitalTypes) {
      if (!digitalType.isSpace()) {
        writeImage(digitalType);
      }
    }
  }

  private void writeImage(DigitalType digitalType) throws IOException {
    Mat mat = OpenCvImage.rawToMat(digitalType.getBytes());
    MatOfByte matOfByte = new MatOfByte();
    Highgui.imencode(".png", mat, matOfByte, new MatOfInt(Highgui.CV_IMWRITE_PNG_COMPRESSION, 0));
    String outImageName = String.format("%s/%d_%d.png", imagesDir, digitalType.y(), digitalType.x());
    FileUtils.writeByteArrayToFile(new File(outImageName), matOfByte.toArray());
  }

  private void writeGraphDot(ClusteringGraph clusteringGraph) throws IOException {
    JGraphTBuilder jGraphTBuilder = new JGraphTBuilder();
    AbstractBaseGraph<GraphItem, JGraphEdge> graph = jGraphTBuilder.getJGraphTFrom(clusteringGraph);
    DOTExporter exporter = new DOTExporter(new IdProvider(), null, new EdgeLabelProvider(), new VertexAttributeProvider(imagesDir), null);
    FileWriter writer = new FileWriter(graphDot);
    exporter.export(writer, graph);
  }

  private void buildPdf(String outputGraphFilename) {
    NativeInterface.launch(GraphvizPath.PATH + "sfdp " + graphDot + " | " + GraphvizPath.PATH + "gvmap -e | /opt/local/bin/neato -Ecolor='#55555522' -n2 -Tpdf", null, outputGraphFilename + ".pdf");
  }
}
