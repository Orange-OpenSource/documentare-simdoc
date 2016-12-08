/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

package com.orange.documentare.core.model.alto.ref.page;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.orange.documentare.core.model.alto.ref.Ref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;


public class PageRef implements Ref {

	/**
	 * Alto document id
	 */
	@Getter
	@Setter
	private String altoId;

	/**
	 * Alto source image id
	 */
	@Getter
	@Setter
	private String altoPageId;

	/**
	 * Image id
	 */
	@Getter
	@Setter
	private String imageId;

	/**
	 * The page number, as defined in the ALTO document
	 */
	@Getter
	@Setter
	private String pageNb;

	/**
	 * Alto page scan horizontal resolution
	 */
	@Getter
	@Setter
	private int hres;

	/**
	 * Alto page scan vertical resolution
	 */
	@Getter
	@Setter
	private int vres;


	/**
	 * The page block
	 */
	@Getter
	@Setter
	private BlockRef pageBlock;

	/**
	 * The blocks, but the page block and the word separators
	 */
	@Getter
	private final List<BlockRef> blocks = new ArrayList<>();

	/**
	 * The blocks hierarchy
	 */
	@Getter
	@Setter
	private HierarchyNodeRef hierarchy;

	/**
	 * The words that can be found in the page
	 */
	@Getter
	@Setter
	private List<WordRef> words = new ArrayList<>();

	/**
	 * The available styles in the page
	 */
	@Getter
	private final List<StyleRef> styles = new ArrayList<>();



  /**
   * "Other" stuff, to be preserved during the deserialize / modify / serialize process
   */
  private final Map<String,Object> other = new HashMap<>();

  // "any getter" needed for serialization
  @JsonAnyGetter
  public Map<String,Object> any ( ) {
      return other;
  }

  @JsonAnySetter
  public void set ( final String name, final Object value ) {
      other.put(name, value);
  }
}
