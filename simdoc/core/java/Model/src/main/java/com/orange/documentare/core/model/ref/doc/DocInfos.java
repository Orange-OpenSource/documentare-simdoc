package com.orange.documentare.core.model.ref.doc;
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
import lombok.Getter;
import lombok.Setter;

/** Infos to link the image to a document */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocInfos {
  /** The ID of the document to which the page belongs */
  private String docId;

  /** The non technical ID for the page */
  private String pageId;

  /** The ID for the page images */
  private String imageId;

  /** Human readable page name */
  private String imageName;

  /** The page number, as defined in the ALTO document */
  private String pageNb;
}
