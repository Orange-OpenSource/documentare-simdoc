package com.orange.documentare.core.system.inputfilesconverter;
/*
 * Copyright (c) 2017 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import java.io.File;

public interface FileConverter {
  void convert(File source, File destination);
}
