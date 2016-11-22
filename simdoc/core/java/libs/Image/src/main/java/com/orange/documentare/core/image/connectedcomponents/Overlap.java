package com.orange.documentare.core.image.connectedcomponents;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.orange.documentare.core.model.ref.segmentation.SegmentationRect;

interface Overlap {
  boolean areOverlapping(SegmentationRect rect1, SegmentationRect rect2);
}
