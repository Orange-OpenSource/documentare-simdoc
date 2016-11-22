package com.orange.documentare.core.model.ref.segmentation;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.orange.documentare.core.model.ref.doc.DocInfos;
import lombok.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The class containing all geometric data for a page: its structure, the
 * position of each of its blocks, and the geometric data for its glyphs.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageSegmentation {

  /** Optional document infos */
  private DocInfos docInfos;

  /** The horizontal resolution, in pixels */
  private Integer hres;

  /** The vertical resolution, in pixels */
  private Integer vres;

  /** The root page block */
  private Block pageBlock;

  /** The blocks hierarchy */
  private HierarchyNode hierarchy;

  /** The blocks, ie physical structure: lines, etc; does not contain the root page block */
  private final Blocks blocks = new Blocks();

  /** Digital types for the whole page */
  private final DigitalTypes digitalTypes = new DigitalTypes();

  /**
   * State flag indicating if digital types distances are already present and ready to be
   * used by the clustering process. If not we will have to compute distances before the
   * clustering process.
   * It is useful to avoid recomputing distances if we want to change the clustering settings.
   */
  private boolean distancesAvailable;

  /** State flag indicating if a clustering has been done and so if clusters id are available for correction propagation */
  private boolean clustersAvailable;

  /** Remove distances and image bytes */
  public void strip() {
    distancesAvailable = false;
    digitalTypes.strip();
  }

  public void saveTo(String filename, boolean prettyPrint) throws IOException {
    OutputStream outputStream = new FileOutputStream(new File(filename));
    ObjectMapper mapper = new ObjectMapper();
    if (prettyPrint) {
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    mapper.writeValue(outputStream, this);
    outputStream.close();
  }
}
