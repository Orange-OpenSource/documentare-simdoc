package com.orange.documentare.core.system.filesid;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
public class FilesIdBuilder {

  public static final String MAP_NAME = "map.json.gz";

  public void createFilesIdDirectory(String srcDir, String destDir, String mapDir) {
    File src = new File(srcDir);
    if (!src.isDirectory()) {
      throw new FilesIdException("[FilesIdBuilder] Source directory does not exists: " + src.getAbsolutePath());
    }

    File dest = createDestination(destDir);

    List<File> srcFiles = createFilesId(src, dest);
    createMap(srcFiles, new File(mapDir));
  }

  public FilesIdMap readMapIn(String absoluteDirectoryPath) {
    ObjectMapper mapper = new ObjectMapper();
    File mapFile = new File(absoluteDirectoryPath + "/" + MAP_NAME);
    try {
      InputStream in = new GZIPInputStream(new FileInputStream(mapFile));
      FilesIdMap map = mapper.readValue(in, FilesIdMap.class);
      in.close();
      return map;
    } catch (IOException e) {
      throw new FilesIdException(String.format("[FilesIdBuilder] failed to read map in directory (%s): %s", absoluteDirectoryPath, e.getMessage()));
    }
  }

  private List<File> createFilesId(File src, File dest) {
    List<File> files =
      FileUtils.listFiles(src, null, true).stream()
      .filter(file -> !file.isHidden())
      .sorted()
      .collect(Collectors.toList());
    for (int id = 0; id < files.size(); id++) {
      Path srcPath = files.get(id).toPath().toAbsolutePath();
      Path destPath = (new File(dest.getAbsolutePath() + "/" + id)).toPath().toAbsolutePath();
      try {
        Files.createSymbolicLink(destPath, srcPath);
      } catch (IOException e) {
        throw new FilesIdException(String.format("[FilesIdBuilder] failed to create symbolic link: %s, %s -> %s", e.getMessage(), srcPath, destPath));
      }
    }
    return files;
  }

  private void createMap(List<File> srcFiles, File mapDir) {
    FilesIdMap map = new FilesIdMap();
    for (int id = 0; id < srcFiles.size(); id++) {
      map.put(id, srcFiles.get(id).getAbsolutePath());
    }

    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
    File mapFile = new File(mapDir.getAbsolutePath() + "/" + MAP_NAME);
    try {
      GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(mapFile));
      writer.writeValue(out, map);
      out.close();
    } catch (IOException e) {
      throw new FilesIdException("[FilesIdBuilder] failed to write map: " + e.getMessage());
    }
  }

  private File createDestination(String destDir) {
    File dest = new File(destDir);
    if (dest.exists()) {
      log.info("[FilesIdBuilder] Directory exists, force delete and recreate: " + destDir);
    }
    try {
      FileUtils.deleteDirectory(dest);
    } catch (IOException e) {
      throw new FilesIdException("[FilesIdBuilder] failed to recreate(delete) destination directory: " + e.getMessage());
    }
    dest.mkdirs();
    return dest;
  }
}
