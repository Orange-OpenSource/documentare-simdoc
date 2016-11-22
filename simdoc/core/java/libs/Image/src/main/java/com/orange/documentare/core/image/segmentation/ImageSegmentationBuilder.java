package com.orange.documentare.core.image.segmentation;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.image.glyphs.Glyphs;
import com.orange.documentare.core.image.linedetection.Line;
import com.orange.documentare.core.image.linedetection.Lines;
import com.orange.documentare.core.image.opencv.OpenCvImage;
import com.orange.documentare.core.image.segmentation.posttreatments.LineNoiseReduction;
import com.orange.documentare.core.image.transformations.ConnectedComponentsToGlyphs;
import com.orange.documentare.core.image.transformations.GlyphsToDigitalTypes;
import com.orange.documentare.core.model.ref.segmentation.*;
import lombok.RequiredArgsConstructor;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** From lines and glyphs, build the image segmentation model */
@RequiredArgsConstructor
public class ImageSegmentationBuilder {
  private final Mat binaryImageMat;
  private final Lines lines;
  private final boolean embedImage;
  private final SegmentationDebug debug;

  /** image segmentation model */
  private final ImageSegmentation imageSegmentation = new ImageSegmentation();

  /** built on the fly and reused later */
  private final Map<Line, Integer> lineIdMap = new HashMap<>();

  private final GlyphsToDigitalTypes glyphsToDigitalTypes = new GlyphsToDigitalTypes();
  private final ConnectedComponentsToGlyphs connectedComponentsToGlyphs = new ConnectedComponentsToGlyphs();
  private final LineNoiseReduction lineNoiseReduction = new LineNoiseReduction();
  private final SpaceDetection spaceDetection = new SpaceDetection();

  private int idCount = 1;

  public ImageSegmentation getImageSegmentation() {
    initResolution();
    updateRootPageBlock();
    addLinesBlocks();
    updatePageHierarchy();
    updatePageDigitalTypes();
    return imageSegmentation;
  }

  private void initResolution() {
    imageSegmentation.setHres(binaryImageMat.width());
    imageSegmentation.setVres(binaryImageMat.height());
  }

  /** root page block: image external rectangle */
  private void updateRootPageBlock() {
    Block block = new Block(0, 0, imageSegmentation.getHres(), imageSegmentation.getVres(), Block.EBlockType.A);
    block.id(getNewId());
    imageSegmentation.setPageBlock(block);
  }

  /** page block contains all lines */
  private void addLinesBlocks() {
    List<Block> blocks = imageSegmentation.getBlocks();
    for (Line line : lines) {
      addLineBlock(blocks, line);
    }
  }

  private void addLineBlock(List<Block> blocks, Line line) {
    Block block = new Block(line.x(), line.y(), line.width(), line.height(), Block.EBlockType.L);
    int id = getNewId();
    block.id(id);
    blocks.add(block);
    lineIdMap.put(line, id);
  }

  /** simple hierarchy: page block with all lines inside */
  private void updatePageHierarchy() {
    HierarchyNode rootNode = new HierarchyNode();
    rootNode.setId(imageSegmentation.getPageBlock().id());
    List<HierarchyNode> pageBlockIns = rootNode.getIns();
    for (Block block : imageSegmentation.getBlocks()) {
      HierarchyNode node = new HierarchyNode();
      node.setId(block.id());
      pageBlockIns.add(node);
    }
    imageSegmentation.setHierarchy(rootNode);
  }

  /** add all lines digital types */
  private void updatePageDigitalTypes() {
    debug.clearGlyphs();
    debug.log("Connected components to glyphs transformation -> " + "Glyphs to digital types transformation -> " + "spaces detection based on digital types");
    DigitalTypes digitalTypes = imageSegmentation.getDigitalTypes();
    for (Line line : lines) {
      addDigitalTypesFromLine(digitalTypes, line);
    }
    debug.saveGlyphs(lines);
  }

  private void addDigitalTypesFromLine(DigitalTypes allDigitalTypes, Line line) {
    Glyphs glyphs = buildGlyphsOf(line);
    debug.addGlyphs(glyphs);
    DigitalTypes lineDigitalTypes = glyphsToDigitalTypes.transform(glyphs, line.y(), line.height());
    spaceDetection.detect(lineDigitalTypes);
    for (DigitalType digitalType : lineDigitalTypes) {
      addDigitalType(allDigitalTypes, digitalType, line);
    }
  }

  private Glyphs buildGlyphsOf(Line line) {
    Glyphs glyphs = connectedComponentsToGlyphs.transform(line.connectedComponents());
    lineNoiseReduction.removeNoise(line, glyphs);
    return glyphs;
  }

  private void addDigitalType(DigitalTypes allDigitalTypes, DigitalType digitalType, Line line) {
    digitalType.setLineId(lineIdMap.get(line));
    if (embedImage && !digitalType.isSpace()) {
      addCropImageToDigitalType(digitalType);
    }
    allDigitalTypes.add(digitalType);
  }

  private void addCropImageToDigitalType(DigitalType digitalType) {
    try {
      digitalType.setBytes(getBytesFor(digitalType));
    } catch (Exception e) {
      System.out.print(e.getMessage());
    }
  }

  private byte[] getBytesFor(SegmentationRect rect) {
    Mat crop = new Mat(binaryImageMat, new Rect(rect.x(), rect.y(), rect.width(), rect.height()));
    return OpenCvImage.getSimDocBytesOf(crop);
  }

  private int getNewId() {
    return idCount++;
  }
}
