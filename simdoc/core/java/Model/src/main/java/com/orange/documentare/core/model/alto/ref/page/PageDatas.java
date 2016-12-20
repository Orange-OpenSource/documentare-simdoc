
package com.orange.documentare.core.model.alto.ref.page;/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Transient datas related to a page. To be injected in the PageRef Json,
 * then removed from the doc description.
 */
@NoArgsConstructor
public final class PageDatas {

	/**
	 * Alto page id
	 */
	@Getter
	@Setter
	private String pageId;

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
}
