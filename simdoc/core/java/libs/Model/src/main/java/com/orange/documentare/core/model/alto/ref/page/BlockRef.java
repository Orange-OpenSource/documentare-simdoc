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

import com.orange.documentare.core.model.alto.constant.EBlockType;

import lombok.Getter;
import lombok.Setter;

public class BlockRef extends AZone {

	/**
	 * The type for the block
	 */
	@Getter
	@Setter
	private EBlockType t;
}
