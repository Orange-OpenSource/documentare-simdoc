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
import com.orange.documentare.core.model.common.AInnerEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/** A node in the page blocks hierarchy of the reference. */
@Getter
@RequiredArgsConstructor(suppressConstructorProperties = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HierarchyNode extends AInnerEntity {
  private final List<HierarchyNode> ins = new ArrayList<>();
}
